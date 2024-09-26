package com.pawalert.backend.domain.comment.controller;


import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.dto.CommentResponse;
import com.pawalert.backend.domain.comment.service.CommentService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/posts/")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("comments/{postId}")
    public ResponseEntity<SuccessResponse<Object>> addComment(@AuthenticationPrincipal CustomUserDetails user,
                                                              @PathVariable Long postId,
                                                              @RequestBody CommentRequestRecord commentDto) {

        return commentService.createComment(commentDto, user, postId);
    }

    // 댓글 조회
    @GetMapping("comments/{postId}")
    public Page<CommentResponse> getComments(@AuthenticationPrincipal CustomUserDetails user,
                                             @PathVariable Long postId,
                                             @PageableDefault(size = 5) Pageable pageable
    ) {
        return commentService.getComments(user, postId, pageable);
    }
}