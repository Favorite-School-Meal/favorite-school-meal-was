package com.example.favoriteschoolmeal.domain.post.service;

import com.example.favoriteschoolmeal.domain.comment.controller.dto.CommentResponse;
import com.example.favoriteschoolmeal.domain.comment.service.CommentService;
import com.example.favoriteschoolmeal.domain.matching.controller.dto.MatchingResponse;
import com.example.favoriteschoolmeal.domain.matching.domain.Matching;
import com.example.favoriteschoolmeal.domain.matching.service.MatchingService;
import com.example.favoriteschoolmeal.domain.member.domain.Member;
import com.example.favoriteschoolmeal.domain.member.service.MemberService;
import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PaginatedPostListResponse;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostDetailResponse;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostSummaryResponse;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.exception.PostException;
import com.example.favoriteschoolmeal.domain.post.exception.PostExceptionType;
import com.example.favoriteschoolmeal.domain.post.repository.PostRepository;
import com.example.favoriteschoolmeal.domain.restaurant.domain.Restaurant;
import com.example.favoriteschoolmeal.domain.restaurant.service.RestaurantService;
import com.example.favoriteschoolmeal.global.security.util.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;
    private final MatchingService matchingService;
    private final RestaurantService restaurantService;
    private final CommentService commentService;

    public PostDetailResponse addPost(final CreatePostRequest request, final Long restaurantId) {
        verifyUserOrAdmin();
        final Member member = getMemberOrThrow(getCurrentMemberId());
        final Matching matching = createMatching(member, request);
        final Restaurant restaurant = findRestaurantByIdOrElseNull(restaurantId);

        final Post post = createPost(request, member, matching, restaurant);
        final Post savedPost = postRepository.save(post);

        MatchingResponse matchingResponse = matchingService.createMatchingResponse(matching);
        List<CommentResponse> commentResponse = new ArrayList<>();
        return PostDetailResponse.from(savedPost, matchingResponse, commentResponse);
    }

    public PostDetailResponse modifyPost(final CreatePostRequest request, final Long postId) {
        verifyUserOrAdmin();
        final Long currentMemberId = getCurrentMemberId();

        final Post post = getPostOrThrow(postId);
        verifyPostOwner(post.getMember().getId(), currentMemberId);

        modifyTitleAndContent(post, request);
        modifyMatchingDetails(post.getMatching(), request);
        final Post savedPost = postRepository.save(post);

        MatchingResponse matchingResponse = matchingService.createMatchingResponse(post.getMatching());
        List<CommentResponse> commentResponse = commentService.createCommentResponseList(savedPost);
        return PostDetailResponse.from(savedPost, matchingResponse, commentResponse);
    }

    @Transactional(readOnly = true)
    public PaginatedPostListResponse findAllPost(final Pageable pageable) {
        final Page<Post> posts = postRepository.findAllOrderByStatusAndTime(pageable);
        summarizePostsIfNotNull(posts);
        List<PostSummaryResponse> postSummaryResponses = posts.stream()
                .map(this::convertToPostSummaryResponse).toList();
        return PaginatedPostListResponse.from(postSummaryResponses, posts.getNumber(),
                posts.getTotalPages(), posts.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PaginatedPostListResponse findAllPostByRestaurantId(final Pageable pageable, final Long restaurantId) {
        final Page<Post> posts = postRepository.findAllByRestaurantIdOrderByStatusAndTime(
                pageable, restaurantId);
        summarizePostsIfNotNull(posts);
        List<PostSummaryResponse> postSummaryResponses = posts.stream()
                .map(this::convertToPostSummaryResponse).toList();
        return PaginatedPostListResponse.from(postSummaryResponses, posts.getNumber(),
                posts.getTotalPages(), posts.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PostDetailResponse findPost(final Long postId) {
        final Post post = getPostOrThrow(postId);
        return PostDetailResponse.from(post,
                matchingService.createMatchingResponse(post.getMatching()),
                commentService.createCommentResponseList(post));
    }

    public void removePost(final Long postId) {
        verifyUserOrAdmin();
        final Post post = getPostOrThrow(postId);
        verifyPostOwnerOrAdmin(post.getMember().getId(), getCurrentMemberId());
        postRepository.delete(post);
    }

    public Optional<Post> findPostOptionally(final Long postId) {
        return postRepository.findById(postId);
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

    private Matching createMatching(final Member member, final CreatePostRequest request) {
        return matchingService.addMatching(member, request.meetingDateTime(),
                request.maxParticipant());
    }

    private void modifyTitleAndContent(final Post post, final CreatePostRequest request) {
        post.modifyTitleAndContent(request.title(), request.content());
    }

    private void modifyMatchingDetails(final Matching matching, final CreatePostRequest request) {
        matchingService.modifyDetails(matching, request.meetingDateTime(),
                request.maxParticipant());
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

    private PostSummaryResponse convertToPostSummaryResponse(final Post post) {
        MatchingResponse matchingResponse = matchingService.createMatchingResponse(
                post.getMatching());
        Integer commentCount = commentService.countCommentByPostId(post.getId());
        return PostSummaryResponse.from(post, matchingResponse, commentCount);
    }

    private Post createPost(final CreatePostRequest request, final Member member,
            final Matching matching, final Restaurant restaurant) {
        return Post.builder()
                .member(member)
                .matching(matching)
                .restaurant(restaurant)
                .title(request.title())
                .content(request.content())
                .build();
    }

    private void verifyUserOrAdmin() {
        SecurityUtils.checkUserOrAdminOrThrow(
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
        return SecurityUtils.getCurrentMemberId(
                () -> new PostException(PostExceptionType.MEMBER_NOT_FOUND));
    }
}