package com.example.favoriteschoolmeal.domain.post.controller;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PaginatedPostListResponse;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostDetailResponse;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostDetailResponse> postAdd(
            @Valid @RequestBody final CreatePostRequest request) {
        final PostDetailResponse response = postService.addPost(request, null);
        return ApiResponse.createSuccess(response);
    }

    @PostMapping("/restaurants/{restaurantId}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostDetailResponse> postAddWithRestaurantId(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreatePostRequest request) {
        final PostDetailResponse response = postService.addPost(request, restaurantId);
        return ApiResponse.createSuccess(response);
    }

    @PutMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostDetailResponse> postModify(
            @PathVariable final Long postId,
            @Valid @RequestBody final CreatePostRequest request) {
        final PostDetailResponse response = postService.modifyPost(request, postId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaginatedPostListResponse> postList(Pageable pageable) {
        final PaginatedPostListResponse response = postService.findAllPost(pageable);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/restaurants/{restaurantId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaginatedPostListResponse> postListByRestaurantId(
            @PathVariable Long restaurantId, Pageable pageable) {
        final PaginatedPostListResponse response = postService.findAllPostByRestaurantId(pageable,
                restaurantId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/members/{memberId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PaginatedPostListResponse> postListByMemberId(
            @PathVariable Long memberId, Pageable pageable) {
        final PaginatedPostListResponse response = postService.findAllPostByMemberId(pageable,
                memberId);
        return ApiResponse.createSuccess(response);
    }

    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostDetailResponse> postDetails(@PathVariable Long postId) {
        final PostDetailResponse response = postService.findPost(postId);
        return ApiResponse.createSuccess(response);
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> postRemove(@PathVariable Long postId) {
        postService.removePost(postId);
        return ApiResponse.createSuccess(null);
    }
}