package com.pawalert.backend.domain.annoucement.model;

import com.pawalert.backend.domain.annoucement.entity.AnnouncementEntity;
import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "공고 요약 DTO")
public record AnnouncementSummaryDto(
        @Schema(description = "공고 ID", example = "1")
        Long id,

        @Schema(description = "공고 제목", example = "사랑스러운 골든 리트리버 '해피' 입양 공고")
        String title,

        @Schema(description = "공고 상태", example = "ACTIVE")
        AnnouncementEntity.AnnouncementStatus status,

        @Schema(description = "보호소 위치 정보")
        LocationRecord shelterLocation,

        @Schema(description = "동물 종류", example = "개")
        String animalType,

        @Schema(description = "발견 장소", example = "서울시 강남구 공원")
        String foundLocation,

        @Schema(description = "입양 가능 시작일", example = "2023-06-01")
        LocalDate adoptionAvailableDate,

        @Schema(description = "공고 만료일", example = "2023-07-01")
        LocalDate announcementExpiryDate,

        @Schema(description = "첫 번째 이미지 URL", example = "https://example.com/images/happy1.jpg")
        String firstImageUrl
) {
    public static AnnouncementSummaryDto fromEntity(AnnouncementEntity entity) {
        return AnnouncementSummaryDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .status(entity.getStatus())
                .shelterLocation(LocationRecord.getLocation(entity.getShelterLocation()))
                .animalType(entity.getAnimalType())
                .foundLocation(entity.getFoundLocation())
                .adoptionAvailableDate(entity.getAdoptionAvailableDate())
                .announcementExpiryDate(entity.getAnnouncementExpiryDate())
                .firstImageUrl(entity.getImageUrls().isEmpty() ? null : entity.getImageUrls().get(0))
                .build();
    }
}