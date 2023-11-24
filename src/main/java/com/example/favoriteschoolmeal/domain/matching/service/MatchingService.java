package com.example.favoriteschoolmeal.domain.matching.service;

import com.example.favoriteschoolmeal.domain.matching.controller.dto.MatchingResponse;
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
import com.example.favoriteschoolmeal.domain.model.RoleType;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingMemberRepository matchingMemberRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

    public MatchingService(final MatchingRepository matchingRepository,
            final MatchingMemberRepository matchingMemberRepository,
            final PostRepository postRepository,
            final MemberService memberService) {
        this.matchingRepository = matchingRepository;
        this.matchingMemberRepository = matchingMemberRepository;
        this.postRepository = postRepository;
        this.memberService = memberService;
    }

    public Matching addMatching(final Member host, final LocalDateTime startDateTime, final LocalDateTime endDateTime,
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

    public void modifyDetails(final Matching matching, LocalDateTime startDateTime, LocalDateTime endDateTime,
            final Integer maxParticipant) {
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
    }

    public void cancelMatchingApplication(final Long postId) {
        verifyUserOrAdmin();
        final Member currentMember = getMemberOrThrow(getCurrentMemberId());
        final Post post = getPostOrThrow(postId);
        final Matching matching = getMatchingFromPost(post);
        final MatchingMember matchingMember = getMatchingMemberOrThrow(matching, currentMember);
        verifyCancellation(matchingMember);
        cancelMatchingMember(matchingMember);
    }

    public void acceptMatchingApplication(final Long postId, final Long applicantMemberId) {
        processMatchingApplication(postId, applicantMemberId, MatchingRequestStatus.ACCEPTED);
    }

    public void rejectMatchingApplication(final Long postId, final Long applicantMemberId) {
        processMatchingApplication(postId, applicantMemberId, MatchingRequestStatus.REJECTED);
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
    }

    public MatchingResponse createMatchingResponse(Matching matching) {
        Integer approvedParticipant = calculateApprovedParticipant(matching);
        return MatchingResponse.from(matching, approvedParticipant);
    }

    public Optional<Matching> findMatchingOptionally(final Long matchingId) {
        return matchingRepository.findById(matchingId);
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
                matchingMemberRepository.countByMatchingAndMatchingRequestStatus(matching, MatchingRequestStatus.ACCEPTED) >= matching.getMaxParticipant();
        final boolean isMatchingOpen = matching.getMatchingStatus().equals(MatchingStatus.IN_PROGRESS);

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

    private void updateMatchingMemberStatus(final MatchingMember matchingMember, final MatchingRequestStatus status) {
        matchingMember.updateMatchingRequestStatus(status);
        matchingMemberRepository.save(matchingMember);
    }

    private void processMatchingApplication(final Long postId, final Long applicantMemberId, final MatchingRequestStatus status) {
        verifyUserOrAdmin();
        final Member host = getMemberOrThrow(getCurrentMemberId());
        final Member applicantMember = getMemberOrThrow(applicantMemberId);
        final Post post = getPostOrThrow(postId);
        verifyPostOwner(post, host);
        final Matching matching = getMatchingFromPost(post);
        verifyMatchingStatus(matching, MatchingStatus.IN_PROGRESS);
        final MatchingMember applicant = getMatchingMemberOrThrow(matching, applicantMember);
        updateMatchingMemberStatus(applicant, status);
    }

    private MatchingMember getMatchingMemberOrThrow(final Matching matching, final Member member) {
        return matchingMemberRepository
                .findByMatchingAndMember(matching, member)
                .orElseThrow(() -> new MatchingException(MatchingExceptionType.MATCHING_MEMBER_NOT_FOUND));
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
