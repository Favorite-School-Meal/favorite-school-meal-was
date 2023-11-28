package com.example.favoriteschoolmeal.domain.matching.service;

import com.example.favoriteschoolmeal.domain.matching.controller.dto.MatchingResponse;
import com.example.favoriteschoolmeal.domain.matching.controller.dto.MemberMatchingCountResponse;
import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.domain.MatchingMember;
import com.example.favoriteschoolmeal.domain.matching.exception.MatchingException;
import com.example.favoriteschoolmeal.domain.matching.exception.MatchingExceptionType;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingMemberRepository;
import com.example.favoriteschoolmeal.domain.matching.repository.MatchingRepository;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.model.MatchingRequestStatus;
import com.example.favoriteschoolmeal.domain.model.MatchingStatus;
import com.example.favoriteschoolmeal.domain.model.NotificationType;
import com.example.favoriteschoolmeal.domain.model.RoleType;
import com.example.favoriteschoolmeal.domain.notification.service.NotificationService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingMemberRepository matchingMemberRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;

    public Matching addMatching(final Member host, final LocalDateTime startDateTime,
            final LocalDateTime endDateTime,
            final Integer maxParticipant) {
        final Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.IN_PROGRESS)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .maxParticipant(maxParticipant)
                .build();
        final Matching savedMatching = matchingRepository.save(matching);
        addHostMemberToMatching(savedMatching, host);
        return savedMatching;
    }

    public void modifyDetails(final Matching matching, final LocalDateTime startDateTime,
            final LocalDateTime endDateTime, final Integer maxParticipant) {
        matching.modifyDetails(startDateTime, endDateTime, maxParticipant);
        matchingRepository.save(matching);
    }

    public void applyMatching(final Long postId) {
        verifyUserOrAdmin();
        final Member applicant = getMemberOrThrow(getCurrentMemberId());
        final Post post = getPostOrThrow(postId);
        final Matching matching = getMatchingFromPost(post);

        checkIfMatchingIsAvailable(matching, applicant);
        addMatchingMember(matching, applicant);
        notificationService.createPostNotification(applicant.getId(), post.getMember().getId(),
                postId,
                NotificationType.MATCHING_REQUESTED);
    }

    public void cancelMatchingApplication(final Long postId) {
        verifyUserOrAdmin();
        final Member applicant = getMemberOrThrow(getCurrentMemberId());
        final Post post = getPostOrThrow(postId);
        final Matching matching = getMatchingFromPost(post);
        final MatchingMember matchingMember = getMatchingMemberOrThrow(matching, applicant);
        verifyCancellation(matchingMember);
        cancelMatchingMember(matchingMember);
        notificationService.createPostNotification(applicant.getId(), post.getMember().getId(),
                postId,
                NotificationType.MATCHING_REQUEST_CANCELED);
    }

    public void acceptMatchingApplication(final Long postId, final Long applicantMemberId) {
        Post post = getPostOrThrow(postId);
        processMatchingApplication(post, applicantMemberId, MatchingRequestStatus.ACCEPTED);
        notificationService.createPostNotification(post.getMember().getId(), applicantMemberId,
                postId,
                NotificationType.MATCHING_REQUEST_ACCEPTED);
    }

    public void rejectMatchingApplication(final Long postId, final Long applicantMemberId) {
        Post post = getPostOrThrow(postId);
        processMatchingApplication(post, applicantMemberId, MatchingRequestStatus.REJECTED);
        notificationService.createPostNotification(post.getMember().getId(), applicantMemberId,
                postId,
                NotificationType.MATCHING_REQUEST_REJECTED);
    }

    public void completeMatching(final Long postId) {
        verifyUserOrAdmin();
        final Member host = getMemberOrThrow(getCurrentMemberId());
        final Post post = getPostOrThrow(postId);
        verifyPostOwner(post, host);
        final Matching matching = getMatchingFromPost(post);
        verifyMatchingStatus(matching, MatchingStatus.IN_PROGRESS);
        matching.completeMatching();
        matchingRepository.save(matching);
        sendNotificationToAcceptedMatchingMembers(matching, post);
    }

    public MatchingResponse createMatchingResponse(final Matching matching) {
        Integer approvedParticipant = calculateApprovedParticipant(matching);
        return MatchingResponse.from(matching, approvedParticipant);
    }

    public void removeMatching(final Long matchingId) {
        findMatchingOptionally(matchingId)
                .ifPresent(matching -> {
                    removeMatchingMembers(matching);
                    matchingRepository.delete(matching);
                });
    }

    /**
     * 주어진 멤버 ID에 해당하는 사용자의 유효한 매칭 횟수를 계산합니다.
     * <p>
     * 이 메소드는 다음과 같은 과정을 거칩니다: 1. 주어진 멤버 ID를 바탕으로 멤버 엔티티를 조회합니다. 멤버를 찾지 못하면 예외를 발생시킵니다. 2. 조회된 멤버와
     * 연관된 모든 매칭 멤버를 가져옵니다. 3. 가져온 매칭 멤버 리스트를 통해 유효한 매칭 횟수를 계산합니다. 4. 계산된 매칭 횟수를 반환합니다.
     *
     * @param memberId 매칭 횟수를 계산할 멤버의 ID
     * @return 멤버의 유효한 매칭 횟수를 담은 응답 객체
     */
    @Transactional(readOnly = true)
    public MemberMatchingCountResponse countMatching(final Long memberId) {
        Member member = getMemberOrThrow(memberId);
        List<MatchingMember> matchingMembers = matchingMemberRepository.findAllByMember(member);
        long count = calculateValidMatchingCount(matchingMembers);
        return MemberMatchingCountResponse.from(count);
    }

    public String findMatchingMemberStatus(Matching matching, Long senderId) {
        Member sender = getMemberOrThrow(senderId);
        MatchingMember matchingMember = getMatchingMemberOrThrow(matching, sender);
        return matchingMember.getMatchingRequestStatus().toString();
    }

    public Optional<Matching> findMatchingOptionally(final Long matchingId) {
        return matchingRepository.findById(matchingId);
    }

    /**
     * 주어진 매칭 멤버 리스트에서 유효한 매칭의 수를 계산합니다.
     * <p>
     * 유효한 매칭의 기준은 다음과 같습니다: 1. 매칭의 상태가 'CLOSED' (완료된 매칭) 여야 합니다. 2. 매칭 멤버의 요청 상태가 'ACCEPTED' (수락된
     * 상태) 여야 합니다. 3. 각 매칭에 최소 2명 이상의 'ACCEPTED' 상태인 매칭 멤버가 있어야 합니다.
     * <p>
     * 이 기준에 따라 매칭 멤버 리스트를 필터링하고, 해당하는 매칭의 수를 계산하여 반환합니다.
     *
     * @param matchingMembers 계산 대상이 되는 매칭 멤버 리스트
     * @return 유효한 매칭의 수
     */
    private long calculateValidMatchingCount(final List<MatchingMember> matchingMembers) {
        return matchingMembers.stream()
                .filter(matchingMember -> matchingMember.getMatching().getMatchingStatus()
                        == MatchingStatus.CLOSED)
                .filter(matchingMember -> matchingMember.getMatchingRequestStatus()
                        == MatchingRequestStatus.ACCEPTED)
                .filter(matchingMember ->
                        matchingMemberRepository.countByMatchingAndMatchingRequestStatus(
                                matchingMember.getMatching(), MatchingRequestStatus.ACCEPTED) >= 2)
                .count();
    }

    private Integer calculateApprovedParticipant(final Matching matching) {
        return Math.toIntExact(matchingMemberRepository.countByMatchingAndMatchingRequestStatus(
                matching, MatchingRequestStatus.ACCEPTED));
    }

    private void checkIfMatchingIsAvailable(final Matching matching, final Member member) {
        if (!isApplicationAvailable(matching, member)) {
            throw new MatchingException(MatchingExceptionType.MATCHING_APPLICATION_DENIED);
        }
    }

    private boolean isApplicationAvailable(final Matching matching, final Member applicant) {
        final boolean alreadyApplied = matchingMemberRepository.findByMatchingAndMember(matching,
                applicant).isPresent();
        final boolean isMatchingFull =
                matchingMemberRepository.countByMatchingAndMatchingRequestStatus(matching,
                        MatchingRequestStatus.ACCEPTED) >= matching.getMaxParticipant();
        final boolean isMatchingOpen = matching.getMatchingStatus()
                .equals(MatchingStatus.IN_PROGRESS);

        return !alreadyApplied && !isMatchingFull && isMatchingOpen;
    }

    private void addHostMemberToMatching(final Matching matching, final Member host) {
        final MatchingMember matchingMember = MatchingMember.builder()
                .member(host)
                .matching(matching)
                .roleType(RoleType.HOST)
                .matchingRequestStatus(MatchingRequestStatus.ACCEPTED)
                .build();
        matchingMemberRepository.save(matchingMember);
    }

    private void addMatchingMember(final Matching matching, final Member applicant) {
        final MatchingMember matchingMember = MatchingMember.builder()
                .member(applicant)
                .matching(matching)
                .roleType(RoleType.GUEST)
                .matchingRequestStatus(MatchingRequestStatus.PENDING)
                .build();
        matchingMemberRepository.save(matchingMember);
    }

    private void cancelMatchingMember(final MatchingMember matchingMember) {
        matchingMemberRepository.delete(matchingMember);
    }

    private void updateMatchingMemberStatus(final MatchingMember matchingMember,
            final MatchingRequestStatus status) {
        matchingMember.updateMatchingRequestStatus(status);
        matchingMemberRepository.save(matchingMember);
    }

    private void processMatchingApplication(final Post post, final Long applicantMemberId,
            final MatchingRequestStatus status) {
        verifyUserOrAdmin();
        final Member host = getMemberOrThrow(getCurrentMemberId());
        final Member applicantMember = getMemberOrThrow(applicantMemberId);
        verifyPostOwner(post, host);
        final Matching matching = getMatchingFromPost(post);
        verifyMatchingStatus(matching, MatchingStatus.IN_PROGRESS);
        final MatchingMember applicant = getMatchingMemberOrThrow(matching, applicantMember);
        updateMatchingMemberStatus(applicant, status);
    }

    private void removeMatchingMembers(final Matching matching) {
        matchingMemberRepository.deleteAll(matchingMemberRepository.findAllByMatching(matching));
    }

    private void sendNotificationToAcceptedMatchingMembers(final Matching matching,
            final Post post) {
        matchingMemberRepository.findByMatchingAndMatchingRequestStatus(matching,
                        MatchingRequestStatus.ACCEPTED)
                .stream()
                .filter(matchingMember -> matchingMember.getRoleType().equals(RoleType.GUEST))
                .forEach(matchingMember -> notificationService.createPostNotification(
                        post.getMember().getId(), matchingMember.getMember().getId(), post.getId(),
                        NotificationType.MATCHING_COMPLETED));
    }

    private MatchingMember getMatchingMemberOrThrow(final Matching matching, final Member member) {
        return matchingMemberRepository
                .findByMatchingAndMember(matching, member)
                .orElseThrow(() -> new MatchingException(
                        MatchingExceptionType.MATCHING_MEMBER_NOT_FOUND));
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new MatchingException(MatchingExceptionType.MEMBER_NOT_FOUND));
    }

    private Matching getMatchingFromPost(final Post post) {
        return Optional.ofNullable(post.getMatching())
                .orElseThrow(() -> new MatchingException(MatchingExceptionType.MATCHING_NOT_FOUND));
    }

    private Long getCurrentMemberId() {
        return SecurityUtils.getCurrentMemberId(
                () -> new MatchingException(MatchingExceptionType.MEMBER_NOT_FOUND));
    }

    private Post getPostOrThrow(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new MatchingException(MatchingExceptionType.POST_NOT_FOUND));
    }

    private void verifyCancellation(final MatchingMember matchingMember) {
        if (!matchingMember.getMatchingRequestStatus().equals(MatchingRequestStatus.PENDING)) {
            throw new MatchingException(MatchingExceptionType.INVALID_OPERATION);
        }
    }

    private void verifyUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(
                () -> new MatchingException(MatchingExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyPostOwner(final Post post, final Member host) {
        if (!post.getMember().equals(host) && !SecurityUtils.isAdmin()) {
            throw new MatchingException(MatchingExceptionType.UNAUTHORIZED_HOST_ACCESS);
        }
    }

    private void verifyMatchingStatus(final Matching matching, final MatchingStatus status) {
        if (!matching.getMatchingStatus().equals(status)) {
            throw new MatchingException(MatchingExceptionType.INVALID_OPERATION);
        }
    }
}
