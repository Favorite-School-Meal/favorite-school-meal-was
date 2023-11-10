package com.example.favoriteschoolmeal.domain.post.controller;

import com.example.favoriteschoolmeal.domain.post.controller.dto.CreatePostRequest;
import com.example.favoriteschoolmeal.domain.post.controller.dto.PostResponse;
import com.example.favoriteschoolmeal.domain.post.domain.Post;
import com.example.favoriteschoolmeal.domain.post.service.PostService;
import com.example.favoriteschoolmeal.domain.post.service.dto.CreatePostCommand;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/posts")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostResponse> postAdd(@RequestBody final CreatePostRequest createPostRequest) {
        final CreatePostCommand createPostCommand = CreatePostCommand.from(createPostRequest);
        final Post post = postService.addPost(createPostCommand);
        final PostResponse postResponse = PostResponse.from(post);

        return ApiResponse.createSuccess(postResponse);
    }
}
