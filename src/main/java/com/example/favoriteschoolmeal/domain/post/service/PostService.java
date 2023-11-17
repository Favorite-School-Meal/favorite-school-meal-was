package com.example.favoriteschoolmeal.domain.post.service;

import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.exception.PostException;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.domain.post.service.dto.CreatePostCommand;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import com.example.favoriteschoolmeal.domain.restaurant.service.RestaurantService;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MatchingService matchingService;
    private final RestaurantService restaurantService;

    public PostService(final PostRepository postRepository, final MemberService memberService,
            final MatchingService matchingService, final RestaurantService restaurantService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
        this.matchingService = matchingService;
        this.restaurantService = restaurantService;
    }

    public Post addPost(final CreatePostCommand command) {
        // TODO: 추후 아래 주석 해제
        // verifyRoleUser();
        final Member member = getMemberOrThrow(getCurrentMemberId());
        final Matching matching = createMatching();
        final Restaurant restaurant = findRestaurantByIdOrElseNull(command.restaurantId());

        final Post post = createPost(command, member, matching, restaurant);
        return postRepository.save(post);
    }

    public Post modifyPost(final Long postId, final CreatePostCommand command) {
        // TODO: 추후 아래 주석 해제
        // verifyRoleUser();
        final Long currentMemberId = getCurrentMemberId();

        final Post post = getPostOrThrow(postId);
        verifyPostOwner(post.getMember().getId(), currentMemberId);

        modifyTitleAndContent(post, command);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAllPost(final Pageable pageable) {
        final Page<Post> posts = postRepository.findAllOrderByStatusAndTime(pageable);
        summarizePostsIfNotNull(posts);
        return posts;
    }

    @Transactional(readOnly = true)
    public Page<Post> findAllPostByRestaurantId(final Long restaurantId, final Pageable pageable) {
        final Page<Post> posts = postRepository.findAllByRestaurantIdOrderByStatusAndTime(restaurantId, pageable);
        summarizePostsIfNotNull(posts);
        return posts;
    }

    @Transactional(readOnly = true)
    public Post findPost(final Long postId) {
        return getPostOrThrow(postId);
    }

    public void removePost(final Long postId) {
        final Post post = getPostOrThrow(postId);
        verifyPostOwnerOrAdmin(post.getMember().getId(), getCurrentMemberId());
        postRepository.delete(post);
    }

    private Post getPostOrThrow(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));
    }

    private Member getMemberOrThrow(final Long memberId) {
        return memberService.findMemberOptionally(memberId)
                .orElseThrow(() -> new PostException(PostExceptionType.MEMBER_NOT_FOUND));
    }

    private Matching getMatchingOrThrow(final Long matchingId) {
        return matchingService.findMatchingOptionally(matchingId)
                .orElseThrow(() -> new PostException(PostExceptionType.MATCHING_NOT_FOUND));
    }

    /**
     * 주어진 restaurantId를 사용하여 Restaurant을 찾습니다. restaurantId가 null이면 null을 반환하고, restaurantId에 해당하는
     * Restaurant이 존재하지 않으면 PostException을 발생시킵니다.
     *
     * @param restaurantId 찾을 Restaurant의 ID
     * @return 찾은 Restaurant 또는 null (restaurantId가 null인 경우)
     * @throws PostException Restaurant을 찾을 수 없는 경우 발생하는 예외
     */
    private Restaurant findRestaurantByIdOrElseNull(final Long restaurantId) {
        return Optional.ofNullable(restaurantId)
                .flatMap(restaurantService::findRestaurantOptionally)
                .orElse(null);
    }

    private Matching createMatching() {
        return matchingService.addMatching()
                .orElseThrow(() -> new PostException(PostExceptionType.MATCHING_NOT_FOUND));
    }

    private void modifyTitleAndContent(final Post post, final CreatePostCommand command) {
        post.modifyTitleAndContent(command.title(), command.content());
    }

    private void summarizeContent(final Post post) {
        String summarizedContent = Optional.of(post.getContent())
                .filter(content -> content.length() > 100)
                .map(content -> content.substring(0, 100) + "...")
                .orElse(post.getContent());
        post.summarizeContent(summarizedContent);
    }

    private void summarizePostsIfNotNull(Page<Post> posts) {
        if (!posts.isEmpty()) {
            posts.forEach(this::summarizeContent);
        }
    }

    private Post createPost(final CreatePostCommand createPostCommand, final Member member,
            final Matching matching, final Restaurant restaurant) {
        return Post.builder()
                .member(member)
                .matching(matching)
                .restaurant(restaurant)
                .title(createPostCommand.title())
                .content(createPostCommand.content())
                .build();
    }

    private void verifyRoleUser() {
        SecurityUtils.checkUserAuthority("ROLE_USER",
                () -> new PostException(PostExceptionType.UNAUTHORIZED_ACCESS));
    }

    private void verifyPostOwner(final Long postOwnerId, final Long currentMemberId) {
        if (!postOwnerId.equals(currentMemberId)) {
            throw new PostException(PostExceptionType.UNAUTHORIZED_ACCESS);
        }
    }

    private void verifyPostOwnerOrAdmin(final Long postOwnerId, final Long currentMemberId) {
        if (!postOwnerId.equals(currentMemberId) && !SecurityUtils.isAdmin()) {
            throw new PostException(PostExceptionType.UNAUTHORIZED_ACCESS);
        }
    }

    private Long getCurrentMemberId() {
        return 1L;
        // TODO: 추후 위 코드 삭제 및 아래 코드 주석 해제
        /*
        return SecurityUtils.getCurrentMemberId(
                () -> new PostException(PostExceptionType.MEMBER_NOT_FOUND));
      */
    }
}