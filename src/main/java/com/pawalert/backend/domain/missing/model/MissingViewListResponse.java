package com.pawalert.backend.domain.missing.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record MissingViewListResponse(
        @Schema(description = "실종글 Id")
        Long missingReportId,
        @Schema(description = "작성자 ID")
        String userId,
        @Schema(description = "작성자 email")
        String email,
        @Schema(description = "제목")
        String title,
        @Schema(description = "실종날짜 2024-12-31T23:59:59")
        LocalDateTime dateLost,
        @Schema(description = "실종 위치")
        String address,
        @Schema(description = "실종 위치 OO 동")
        String addressDetail1,
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
        @Schema(description = "포상금액")
        int rewardAmount

) {
}
