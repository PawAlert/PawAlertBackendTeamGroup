package com.pawalert.backend.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentResponse(
        @Schema(description = "댓글 ID", example = "abc123")
        String id,

        @Schema(description = "사용자 ID", example = "user456")
        String userId,

        @Schema(description = "실종글 ID", example = "1")
        Long missingReportId,

        @Schema(description = "댓글 내용", example = "이 강아지를 봤어요!")
        String content,

        @Schema(description = "삭제 여부", example = "false")
        boolean isDeleted,

        @Schema(description = "댓글 작성 시간", example = "2024-10-12T14:30:00")
        LocalDateTime timestamp,

        @Schema(description = "내 댓글 여부", example = "true")
        boolean isCommentMine
) {
}
