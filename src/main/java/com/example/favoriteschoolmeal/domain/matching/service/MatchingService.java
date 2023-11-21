package com.example.favoriteschoolmeal.domain.matching.service;

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
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final MatchingMemberRepository matchingMemberRepository;
    private final PostService postService;
    private final MemberService memberService;

    public MatchingService(final MatchingRepository matchingRepository,
            final MatchingMemberRepository matchingMemberRepository, final PostService postService,
            final MemberService memberService) {
        this.matchingRepository = matchingRepository;
        this.matchingMemberRepository = matchingMemberRepository;
        this.postService = postService;
        this.memberService = memberService;
    }

    public Matching addMatching(final Member host, final LocalDateTime meetingDateTime, final Integer maxParticipant) {
        final Matching matching = Matching.builder()
                .matchingStatus(MatchingStatus.IN_PROGRESS)
                .maxParticipant(maxParticipant)
                .meetingDateTime(meetingDateTime)
                .build();
        final Matching savedMatching = matchingRepository.save(matching);
        addHostMemberToMatching(savedMatching, host);
        return savedMatching;
    }

    public void modifyDetails(final Matching matching, final LocalDateTime meetingDateTime,
            final Integer maxParticipant) {
        matching.modifyDetails(meetingDateTime, maxParticipant);
        matchingRepository.save(matching);
    }

    public Matching applyMatching(final Long postId) {
        Member applicant = getCurrentMemberOrThrow();
        Post post = getPostOrThrow(postId);
        Matching matching = getMatchingFromPost(post);

        checkIfMatchingIsAvailable(matching, applicant);
        addMatchingMember(matching, applicant);

        return matching;
    }

    public Optional<Matching> findMatchingOptionally(final Long matchingId) {
        return matchingRepository.findById(matchingId);
    }

    private void checkIfMatchingIsAvailable(Matching matching, Member member) {
        if (!isApplicationAvailable(matching, member)) {
            throw new MatchingException(MatchingExceptionType.MATCHING_APPLICATION_DENIED);
        }
    }

    private boolean isApplicationAvailable(Matching matching, Member applicant) {
        boolean alreadyApplied = matchingMemberRepository.findByMatchingAndMember(matching, applicant).isPresent();
        boolean isMatchingFull = matchingMemberRepository.countByMatching(matching) >= matching.getMaxParticipant();
        boolean isMatchingOpen = matching.getMatchingStatus().equals(MatchingStatus.IN_PROGRESS);

        return !alreadyApplied && !isMatchingFull && isMatchingOpen;
    }

    private void addHostMemberToMatching(Matching matching, Member host) {
        MatchingMember matchingMember = MatchingMember.builder()
                .member(host)
                .matching(matching)
                .roleType(RoleType.HOST)
                .matchingRequestStatus(MatchingRequestStatus.ACCEPTED)
                .build();
        matchingMemberRepository.save(matchingMember);
    }

    private void addMatchingMember(Matching matching, Member applicant) {
        MatchingMember matchingMember = MatchingMember.builder()
                .member(applicant)
                .matching(matching)
                .roleType(RoleType.GUEST)
                .matchingRequestStatus(MatchingRequestStatus.PENDING)
                .build();
        matchingMemberRepository.save(matchingMember);
    }

    private Member getCurrentMemberOrThrow() {
        return memberService.findMemberOptionally(getCurrentMemberId())
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
        return postService.findPostOptionally(postId)
                .orElseThrow(() -> new MatchingException(MatchingExceptionType.POST_NOT_FOUND));
    }

    private void verifyRoleUser() {
        SecurityUtils.checkUserAuthority("ROLE_USER",
                () -> new MatchingException(MatchingExceptionType.UNAUTHORIZED_ACCESS));
    }
}
