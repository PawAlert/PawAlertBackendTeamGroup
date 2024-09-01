package com.pawalert.backend.domain.missing.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MissingReportRecord(
        @Schema(description = "제목")
        String title,
        @Schema(description = "게시글 내용")
        String content,
        @Schema(description = "실종날짜 2024-12-31T23:59:59")
        LocalDateTime dateLost,
        @Schema(description = "실종 위치 위도")
        BigDecimal latitude,
        @Schema(description = "실종 위치 경도")
        BigDecimal longitude,
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

        String extraAddress,
        @Schema(description = "포상 금액")
        int rewardAmount,
        @Schema(description = "포상 내용")
        String rewardDescription,

        String postcode,
        @Schema(description = "실종된 주소")
        String address,
        // todo : 이름 수정하자!
        @Schema(description = "상세 주소1")
        String addressDetail1

//        @Schema(description = "펫 사진 URL")
//                List<String> petImageUrl,
) {
}
