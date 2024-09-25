package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MissingReportRecord(
        @Schema(description = "제목")
        String title,
        @Schema(description = "게시글 내용")
        String content,
        @Schema(description = "실종날짜 2024-12-31T23:59:59")
        LocalDate dateLost,
        @Schema(description = "연락처1")
        String contact1,
        @Schema(description = "연락처2")
        String contact2,
        @Schema(description = "특징 및 설명")
        String description,
        @Schema(description = "상태 예: 실종 중, 발견됨, 종료 = 기본값 MISSING")
        MissingStatus status,
        @Schema(description = "펫 칩 ID")
        String microchipId,
        @Schema(description = "펫 이름")
        String petName,
        @Schema(description = "펫 품종")
        String species,
        @Schema(description = "펫 색상")
        String petColor,
        @Schema(description = "펫 나이")
        int age,
        @Schema(description = "펫 성별")
        String petGender,
        @Schema(description = "펫 특징")
        String petDescription,
        @Schema(description = "위도/경도")
        LocataionRecord locataionRecord

) {
}
