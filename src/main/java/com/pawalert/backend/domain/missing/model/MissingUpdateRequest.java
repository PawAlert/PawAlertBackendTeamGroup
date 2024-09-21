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
        @Schema(description = "펫 정보수정")
        String petDescription,
        @Schema(description = "연락처1")
        String contact1,
        @Schema(description = "연락처2")
        String contact2,
        @Schema(description = "동물 종")
        String petSpecies,
        @Schema(description = "마이크로칩 ID")
        String microchipId,
        @Schema(description = "특징 및 설명")
        String description,
        @Schema(description = "실종 상태 변경")
        String missingStatus
) {
}
