package com.example.favoriteschoolmeal.domain.post.service;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
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
        final Member member = getMember(createPostCommand.getMemberId());
        final Matching matching = getMatching(createPostCommand.getMatchingId());

        return postRepository.save(buildPost(member, matching, createPostCommand));
    }

    private Member getMember(final Long memberId) {
        return memberService.findMemberById(createPostCommand.getMemberId())
                .orElseThrow(() -> new PostNotFoundException(PostExceptionType.Member_NOT_FOUND));
    }

    private Matching getMatching(final Long matchingId) {
        return matchingService.findMatchingById(createPostCommand.getMatchingId())
                .orElseThrow(() -> new PostNotFoundException(PostExceptionType.MATHCING_NOT_FOUND));
    }

    private Post buildPost(final Member member, final Matching matching,
            final CreatePostCommand createPostCommand) {
        return Post.builder()
                .member(member)
                .matching(matching)
                .title(createPostCommand.getTitle())
                .content(createPostCommand.getContent())
                .build();
    }
}
