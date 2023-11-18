package com.example.favoriteschoolmeal.domain.post.controller;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostResponse;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.domain.post.service.dto.CreatePostCommand;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
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

    private ApiResponse<PostResponse> postAddAndRespond(final CreatePostRequest request,
            final Long restaurantId) {
        final Post post = postService.addPost(
                CreatePostCommand.of(request, restaurantId));
        return ApiResponse.createSuccess(PostResponse.from(post));
    }
}