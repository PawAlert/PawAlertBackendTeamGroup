package com.pawalert.backend.domain.comment.dto;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record CommentResponse(
        String id,
        String userId,
        Long missingReportId,
        String content,
        boolean isDeleted,
        LocalDateTime timestamp,
        boolean isCommentMine
) {
}
