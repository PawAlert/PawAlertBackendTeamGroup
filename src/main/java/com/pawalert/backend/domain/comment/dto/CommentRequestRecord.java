package com.pawalert.backend.domain.comment.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record CommentRequestRecord(
        @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
        String content,
        @Schema(description = "user ID", example = "1")
        Long userId,
        @Schema(description = "댓글 비밀글 여부", example = "false")
        Boolean isSecret,
        @Schema(description = "댓글 비밀번호", example = "1234")
        String password
) {
}
