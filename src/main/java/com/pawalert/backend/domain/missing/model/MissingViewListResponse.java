package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record MissingViewListResponse(
        @Schema(description = "실종글 Id", example = "1")
        Long missingReportId,

        @Schema(description = "작성자 ID", example = "123")
        Long userId,

        @Schema(description = "제목", example = "우리 강아지를 찾습니다")
        String title,

        @Schema(description = "실종날짜", example = "2024-12-31")
        LocalDate dateLost,

        @Schema(description = "위치코드", example = "12345")
        String postcode,

        @Schema(description = "주소", example = "경기도 시흥시 현대아파트")
        String address,

        @Schema(description = "상세주소", example = "101동 202호")
        String addressDetail,

        @Schema(description = "실종상태", example = "MISSING")
        String missingStatus,

        @Schema(description = "펫 이름", example = "바둑이")
        String petName,

        @Schema(description = "펫 품종", example = "시츄")
        String species,

        @Schema(description = "펫 색상", example = "흰색")
        String petColor,

        @Schema(description = "펫 나이", example = "3")
        int age,

        @Schema(description = "펫 성별", example = "수컷")
        String petGender,

        @Schema(description = "펫 이미지 url", example = "http://example.com/image.jpg")
        String petImageUrls,

        @Schema(description = "설명", example = "검정색 털에 하얀 발")
        String content,

        @Schema(description = "연락처", example = "010-1234-5678")
        String contact
) {
    public static MissingViewListResponse from(MissingReportEntity missingReport) {
        String firstImageUrl = missingReport.getMissingPetImages().get(0).getMissingPhotoUrl();

        return new MissingViewListResponse(
                missingReport.getId(),
                missingReport.getUser().getId(),
                missingReport.getMissingTitle(),
                missingReport.getDateLost(),
                missingReport.getLocation().getPostcode(),
                missingReport.getLocation().getAddress(),
                missingReport.getLocation().getAddressDetail(),
                missingReport.getStatus().name(),
                missingReport.getMissingPetName(),
                missingReport.getMissingSpecies(),
                missingReport.getMissingPetColor(),
                missingReport.getMissingPetAge(),
                missingReport.getMissingPetGender(),
                firstImageUrl,
                missingReport.getMissingPetDescription(),
                missingReport.getContact1()
        );
    }

    public static List<MissingViewListResponse> fromList(List<MissingReportEntity> missingReports) {
        return missingReports.stream()
                .filter(missingReport -> !missingReport.isDeleted())
                .map(MissingViewListResponse::from)
                .toList();
    }
}
