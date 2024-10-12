package com.pawalert.backend.domain.comment.controller;


import com.pawalert.backend.domain.comment.dto.CommentRequestRecord;
import com.pawalert.backend.domain.comment.dto.CommentResponse;
import com.pawalert.backend.domain.comment.service.CommentService;
import com.pawalert.backend.global.httpstatus.exception.SuccessResponse;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "댓글 작성, 조회 API 입니다", description = "댓글 관련 API")

public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("comments/{postId}")
    @Operation(summary = "댓글 작성하기", description = "댓글 작성 API 입니다.")
    public ResponseEntity<SuccessResponse<Object>> addComment(@AuthenticationPrincipal CustomUserDetails user,
                                                              @PathVariable Long postId,
                                                              @RequestBody CommentRequestRecord commentDto) {

        return commentService.createComment(commentDto, user, postId);
    }

    // 댓글 조회
    @GetMapping("comments/{postId}")
    @Operation(summary = "게시글 내의 댓글을 조회합니다.", description = "게시글 내부 댓글을 조회하는 API 입니다.")
    public Page<CommentResponse> getComments(@AuthenticationPrincipal CustomUserDetails user,
                                             @PathVariable Long postId,
                                             @PageableDefault(size = 5) Pageable pageable
    ) {
        return commentService.getComments(user, postId, pageable);
    }

}