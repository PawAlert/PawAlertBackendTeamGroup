package com.pawalert.backend.domain.missing.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChangeMissingStatusRecord(
        @Schema(description = "변경할 게시글 ID", example = "1")
        Long missingReportId, // 실종글 ID (자동 증가)

        @Schema(description = "변경할 실종 상태", example = "FOUND")
        MissingStatus status // 변경할 상태 (예: MISSING, FOUND 등)
) {
}
