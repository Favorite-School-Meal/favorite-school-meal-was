package com.example.favoriteschoolmeal.domain.post.service;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.domain.post.exception.PostNotFoundException;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.domain.post.service.dto.CreatePostCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MatchingService matchingService;

    public PostService(final PostRepository postRepository, final MemberService memberService,
            final MatchingService matchingService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
        this.matchingService = matchingService;
    }

    public Post addPost(final CreatePostCommand createPostCommand) {
        final Member member = getMember(createPostCommand.memberId());
        final Matching matching = getMatching(createPostCommand.matchingId());

        return postRepository.save(createPostCommand.toEntity(member, matching));
    }

    private Member getMember(final Long memberId) {
        return memberService.findMemberById(memberId)
                .orElseThrow(() -> new PostNotFoundException(PostExceptionType.MEMBER_NOT_FOUND));
    }

    private Matching getMatching(final Long matchingId) {
        return matchingService.findMatchingById(matchingId)
                .orElseThrow(() -> new PostNotFoundException(PostExceptionType.MATCHING_NOT_FOUND));
    }
}
