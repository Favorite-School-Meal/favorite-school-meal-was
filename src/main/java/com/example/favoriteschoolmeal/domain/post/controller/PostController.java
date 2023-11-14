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

    // TODO: JWT 인증 기능 완성 후, 사용자 인증 정보를 이용하여 게시물 작성 권한을 확인하도록 수정
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostResponse> postAdd(
            @RequestBody final CreatePostRequest createPostRequest) {
        final Long currentMemberId = getCurrentMemberId();
        final CreatePostCommand command = CreatePostCommand.of(createPostRequest, currentMemberId, null);
        final Post post = postService.addPost(command);

        return ApiResponse.createSuccess(PostResponse.from(post));
    }

    // TODO: JWT 인증 기능 완성 후, 사용자 인증 정보를 이용하여 게시물 작성 권한을 확인하도록 수정
    @PostMapping("/restaurants/{restaurantId}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PostResponse> postAddWithRestaurantId(
            @PathVariable Long restaurantId,
            @RequestBody CreatePostRequest createPostRequest) {
        final Long currentMemberId = getCurrentMemberId();
        final CreatePostCommand command = CreatePostCommand.of(createPostRequest, currentMemberId, restaurantId);
        final Post post = postService.addPost(command);

        return ApiResponse.createSuccess(PostResponse.from(post));
    }

    private Long getCurrentMemberId() {
        // TODO: JWT 인증 기능 완성 후, 현재 사용자의 ID를 반환하도록 구현
        return 1L;
    }
}