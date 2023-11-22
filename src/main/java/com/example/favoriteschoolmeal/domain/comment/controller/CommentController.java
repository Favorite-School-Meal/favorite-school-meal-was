package com.example.favoriteschoolmeal.domain.comment.controller;

import com.example.favoriteschoolmeal.domain.comment.controller.dto.CommentResponse;
import com.example.favoriteschoolmeal.domain.comment.controller.dto.CreateCommentRequest;
import com.example.favoriteschoolmeal.domain.comment.domain.Comment;
import com.example.favoriteschoolmeal.domain.comment.service.CommentService;
import com.example.favoriteschoolmeal.domain.comment.service.dto.CreateCommentCommand;
import com.example.favoriteschoolmeal.global.common.response.ApiResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/posts/{postId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentResponse> commentAdd(
            @PathVariable final Long postId,
            @RequestBody final CreateCommentRequest request) {
        final Comment comment = commentService.addComment(
                CreateCommentCommand.of(request, postId));
        return ApiResponse.createSuccess(CommentResponse.from(comment));
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CommentResponse> commentModify(
            @PathVariable final Long postId,
            @PathVariable final Long commentId,
            @RequestBody final CreateCommentRequest request) {
        final Comment comment = commentService.modifyComment(
                commentId, CreateCommentCommand.of(request, postId));
        return ApiResponse.createSuccess(CommentResponse.from(comment));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CommentResponse>> commentList(
            @PathVariable final Long postId) {
        final List<Comment> comments = commentService.findAllPost(postId);
        final List<CommentResponse> commentResponses = CommentResponse.listFrom(comments);
        return ApiResponse.createSuccess(commentResponses);
    }
}
