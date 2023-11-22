package com.example.favoriteschoolmeal.domain.post.controller;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostListResponse;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostResponse;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.domain.post.service.dto.CreatePostCommand;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.data.domain.Page;
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
    public ApiResponse<PostResponse> postAdd(
            @RequestBody final CreatePostRequest request) {
        return postAddAndRespond(request, null);
    }

    @PostMapping("/restaurants/{restaurantId}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostResponse> postAddWithRestaurantId(
            @PathVariable Long restaurantId,
            @RequestBody CreatePostRequest request) {
        return postAddAndRespond(request, restaurantId);
    }

    @PutMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostResponse> postModify(
            @PathVariable final Long postId,
            @RequestBody final CreatePostRequest request) {
        final Post post = postService.modifyPost(
                postId, CreatePostCommand.of(request, null));
        return ApiResponse.createSuccess(PostResponse.from(post));
    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostListResponse> postList(Pageable pageable) {
        final Page<Post> posts = postService.findAllPost(pageable);
        return postListAndRespond(posts);
    }

    @GetMapping("/restaurants/{restaurantId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostListResponse> postListByRestaurantId(
            @PathVariable Long restaurantId,
            Pageable pageable) {
        final Page<Post> posts = postService.findAllPostByRestaurantId(restaurantId, pageable);
        return postListAndRespond(posts);
    }

    @GetMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<PostResponse> postDetails(@PathVariable Long postId) {
        final Post post = postService.findPost(postId);
        return ApiResponse.createSuccess(PostResponse.from(post));
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> postRemove(@PathVariable Long postId) {
        postService.removePost(postId);
        return ApiResponse.createSuccess(null);
    }

    private ApiResponse<PostResponse> postAddAndRespond(final CreatePostRequest request,
            final Long restaurantId) {
        final Post post = postService.addPost(
                CreatePostCommand.of(request, restaurantId));
        return ApiResponse.createSuccess(PostResponse.from(post));
    }

    private ApiResponse<PostListResponse> postListAndRespond(final Page<Post> posts) {
        final Page<PostResponse> postResponses = posts.map(PostResponse::from);
        final PostListResponse postListResponse = PostListResponse.from(postResponses);
        return ApiResponse.createSuccess(postListResponse);
    }
}