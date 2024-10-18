package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record MissingReportNewListResponse(
        @Schema(description = "실종글 Id", example = "1")
        Long missingReportId,

        @Schema(description = "제목", example = "우리 강아지를 찾습니다")
        String missingTitle,

        String missingStatus,

        @Schema(description = "펫 이름", example = "바둑이")
        String petName,

        @Schema(description = "위치 정보")
        LocationRecord locationRecord,

        @Schema(description = "실종날짜", example = "2024-12-31")
        LocalDate dateLost,

        @Schema(description = "펫 이미지 url", example = "http://example.com/image.jpg")
        String petImageUrl
) {

    public static MissingReportNewListResponse from(MissingReportEntity missingReport) {
        String firstImageUrl = missingReport.getMissingPetImages().isEmpty() ?
                null : missingReport.getMissingPetImages().get(0).getMissingPhotoUrl();

        return new MissingReportNewListResponse(
                missingReport.getId(),
                missingReport.getMissingTitle(),
                missingReport.getStatus().toString(),
                missingReport.getMissingPetName(),
                LocationRecord.getLocation(missingReport.getLocation()),
                missingReport.getDateLost(),
                firstImageUrl
        );
    }
}