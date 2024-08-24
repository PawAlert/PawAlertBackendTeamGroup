package com.pawalert.backend.domain.missing.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MissingUpdateRequest(
        @Schema(description = "실종글 Id")
        Long missingReportId,
        @Schema(description = "제목")
        String title,
        @Schema(description = "게시글 내용")
        String content,
        @Schema(description = "특징 및 설명")
        String description,
        @Schema(description = "상태 예: 실종 중, 발견됨, 종료 = 기본값 MISSING")
        MissingStatus status,
        @Schema(description = "포상 금액")
        int rewardAmount,
        @Schema(description = "이미지 삭제 List")
        List<Long> deleteImageIdList
) {
}
