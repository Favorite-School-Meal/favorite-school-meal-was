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

    public Post addPost(final CreatePostCommand command) {
        final Member member = findMemberByMemberId(command.memberId());
        final Matching matching = createMatching();

        final Post post = createPost(command, member, matching);
        return postRepository.save(post);
    }

    private Member findMemberByMemberId(final Long memberId) {
        return memberService.findMemberById(memberId)
                .orElseThrow(() -> new PostNotFoundException(PostExceptionType.MEMBER_NOT_FOUND));
    }

    private Matching findMatchingByMatchingId(final Long matchingId) {
        return matchingService.findMatchingById(matchingId)
                .orElseThrow(() -> new PostNotFoundException(PostExceptionType.MATCHING_NOT_FOUND));
    }

    private Matching createMatching() {
        return matchingService.addMatching();
    }

    private Post createPost(final CreatePostCommand createPostCommand, final Member member, final Matching matching) {
        return Post.builder()
                .member(member)
                .matching(matching)
                .title(createPostCommand.title())
                .content(createPostCommand.content())
                .build();
    }
}
