package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
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
    // MissingReport 객체를 MissingViewListResponse로 변환하는 정적 메서드 추가
    public static MissingViewListResponse from(MissingReportEntity missingReport) {
        String firstImageUrl = missingReport.getMissingPetImages().get(0).getMissingPhotoUrl();

        return new MissingViewListResponse(
                missingReport.getId(),
                missingReport.getUser().getId(),
                missingReport.getTitle(),
                missingReport.getDateLost(),
                missingReport.getLocation().getPostcode(),
                missingReport.getLocation().getAddress(),
                missingReport.getLocation().getAddressDetail(),
                missingReport.getStatus().name(),
                missingReport.getPet().getPetName(),
                missingReport.getPet().getSpecies(),
                missingReport.getPet().getColor(),
                missingReport.getPet().getAge(),
                missingReport.getPet().getGender(),
                firstImageUrl,
                missingReport.getDescription(),
                missingReport.getContact1()
        );
    }

    // 리스트 변환도 지원하는 유틸리티 메서드 추가
    public static List<MissingViewListResponse> fromList(List<MissingReportEntity> missingReports) {
        return missingReports.stream()
                .filter(missingReport -> !missingReport.isDeleted()) // deleted = false인 항목만 필터링
                .map(MissingViewListResponse::from)
                .toList(); // 스트림을 다시 List로 변환
    }
}
