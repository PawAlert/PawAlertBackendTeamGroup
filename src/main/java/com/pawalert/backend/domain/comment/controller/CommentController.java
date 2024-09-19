package com.pawalert.backend.domain.comment.controller;


import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.service.CommentService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class CommentController {

private final CommentService commentService;


    @PostMapping("/{postId}/comments")
    public ResponseEntity<SuccessResponse<Object>> addComment(@AuthenticationPrincipal CustomUserDetails user,
                                                              @PathVariable Long postId,
                                                              @RequestBody CommentRequestRecord commentDto) {

        return commentService.createComment(commentDto, user, postId);

    }
}