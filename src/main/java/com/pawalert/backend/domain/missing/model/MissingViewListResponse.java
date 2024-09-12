package com.pawalert.backend.domain.missing.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MissingViewListResponse(
        @Schema(description = "실종글 Id")
        Long missingReportId,
        @Schema(description = "작성자 ID")
        Long userId,
        @Schema(description = "제목")
        String title,
        @Schema(description = "실종날짜 2024-12-31T23:59:59")
        LocalDate dateLost,
        @Schema(description = "위치코드")
        String postcode,
        @Schema(description = "경기도 시흥시 현대아파트")
        String address,
        @Schema(description = "상세주소")
        String addressDetail,
        @Schema(description = "실종상태")
        String missingStatus,
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
        @Schema(description = "펫 이미지 url")
        String petImageUrls,
        @Schema(description = "설명")
        String content,
        @Schema(description = "연락처")
        String contact

) {
}
