package com.pawalert.backend.domain.annoucement.model;

import com.pawalert.backend.domain.annoucement.entity.AnnouncementEntity;
import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "공고 상세 DTO")
public record AnnouncementDetailDto(
        @Schema(description = "공고 ID", example = "1")
        Long id,

        @Schema(description = "공고 제목", example = "사랑스러운 골든 리트리버 '해피' 입양 공고")
        String title,

        @Schema(description = "공고 내용", example = "2살 된 온순한 골든 리트리버 '해피'가 새로운 가족을 찾고 있습니다.")
        String content,

        @Schema(description = "공고 상태", example = "ACTIVE")
        AnnouncementEntity.AnnouncementStatus status,

        @Schema(description = "보호소 이름", example = "서울 동물 사랑 보호소")
        String shelterName,

        @Schema(description = "보호소 위치 정보")
        LocationRecord shelterLocation,

        @Schema(description = "동물 종류", example = "개")
        String animalType,

        @Schema(description = "품종", example = "골든 리트리버")
        String breed,

        @Schema(description = "동물 이름", example = "해피")
        String animalName,

        @Schema(description = "추정 나이", example = "2")
        Integer estimatedAge,

        @Schema(description = "성별", example = "수컷")
        String gender,

        @Schema(description = "중성화 여부", example = "true")
        Boolean isNeutered,

        @Schema(description = "체중", example = "30.5")
        Double weight,

        @Schema(description = "색상", example = "황금색")
        String color,

        @Schema(description = "발견 장소", example = "서울시 강남구 공원")
        String foundLocation,

        @Schema(description = "특이사항", example = "다른 개들과 잘 어울리며, 기본적인 훈련이 되어 있습니다.")
        String specialNotes,

        @Schema(description = "입양 가능 시작일", example = "2023-06-01")
        LocalDate adoptionAvailableDate,

        @Schema(description = "공고 만료일", example = "2023-07-01")
        LocalDate announcementExpiryDate,

        @Schema(description = "입양 요구사항", example = "넓은 공간과 정기적인 산책이 가능한 가정을 희망합니다.")
        String adoptionRequirements,

        @Schema(description = "이미지 URL 목록", example = "[\"https://example.com/images/happy1.jpg\", \"https://example.com/images/happy2.jpg\"]")
        List<String> imageUrls
) {
    public static AnnouncementDetailDto fromEntity(AnnouncementEntity entity) {
        return AnnouncementDetailDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .status(entity.getStatus())
                .shelterName(entity.getShelterName())
                .shelterLocation(LocationRecord.getLocation(entity.getShelterLocation()))
                .animalType(entity.getAnimalType())
                .breed(entity.getBreed())
                .animalName(entity.getAnimalName())
                .estimatedAge(entity.getEstimatedAge())
                .gender(entity.getGender())
                .isNeutered(entity.getIsNeutered())
                .weight(entity.getWeight())
                .color(entity.getColor())
                .foundLocation(entity.getFoundLocation())
                .specialNotes(entity.getSpecialNotes())
                .adoptionAvailableDate(entity.getAdoptionAvailableDate())
                .announcementExpiryDate(entity.getAnnouncementExpiryDate())
                .adoptionRequirements(entity.getAdoptionRequirements())
                .imageUrls(entity.getImageUrls())
                .build();
    }
}