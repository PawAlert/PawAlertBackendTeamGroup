package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record MissingReportNewListResponse(
        @Schema(description = "실종글 Id", example = "1")
        Long missingReportId,
        @Schema(description = "제목", example = "우리 강아지를 찾습니다")
        String missingTitle,
        @Schema(description = "펫 이름", example = "바둑이")
        String petName,
        @Schema(description = "주소", example = "경기도 시흥시 현대아파트")
        String address,

        @Schema(description = "상세주소", example = "101동 202호")
        String addressDetail,

        @Schema(description = "실종상태", example = "MISSING")
        String missingStatus,
        @Schema(description = "실종날짜", example = "2024-12-31")
        LocalDate dateLost,

        @Schema(description = "펫 이미지 url", example = "http://example.com/image.jpg")
        String petImageUrls
) {

    public static MissingReportNewListResponse from(MissingReportEntity missingReport) {
        String firstImageUrl = missingReport.getMissingPetImages().get(0).getMissingPhotoUrl();

        return new MissingReportNewListResponse(
                missingReport.getId(),
                missingReport.getMissingTitle(),
                missingReport.getMissingPetName(),
                missingReport.getLocation().getAddress(),
                missingReport.getLocation().getAddressDetail(),
                missingReport.getStatus().toString(),
                missingReport.getDateLost(),
                firstImageUrl
        );
    }
}
