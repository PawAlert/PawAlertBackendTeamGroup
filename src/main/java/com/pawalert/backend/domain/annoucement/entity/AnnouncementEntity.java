package com.pawalert.backend.domain.annoucement.entity;

import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공고 엔티티")
public class AnnouncementEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "공고 ID", example = "1")
    private Long id;

    @Schema(description = "기관 ID", example = "shelter123")
    private String officialId;

    @Schema(description = "공고 제목", example = "사랑스러운 골든 리트리버 '해피' 입양 공고")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "공고 내용", example = "2살 된 온순한 골든 리트리버 '해피'가 새로운 가족을 찾고 있습니다.")
    private String content;

    @Enumerated(EnumType.STRING)
    @Schema(description = "공고 상태", example = "ACTIVE")
    private AnnouncementStatus status;

    @Schema(description = "보호소 이름", example = "서울 동물 사랑 보호소")
    private String shelterName;

    @Embedded
    @Schema(description = "보호소 위치 정보")
    private Location shelterLocation;

    @Schema(description = "동물 종류", example = "개")
    private String animalType;

    @Schema(description = "품종", example = "골든 리트리버")
    private String breed;

    @Schema(description = "동물 이름", example = "해피")
    private String animalName;

    @Schema(description = "추정 나이", example = "2")
    private Integer estimatedAge;

    @Schema(description = "성별", example = "수컷")
    private String gender;

    @Schema(description = "중성화 여부", example = "true")
    private Boolean isNeutered;

    @Schema(description = "체중", example = "30.5")
    private Double weight;

    @Schema(description = "색상", example = "황금색")
    private String color;

    @Schema(description = "발견 장소", example = "서울시 강남구 공원")
    private String foundLocation;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "특이사항", example = "다른 개들과 잘 어울리며, 기본적인 훈련이 되어 있습니다.")
    private String specialNotes;

    @Schema(description = "입양 가능 시작일", example = "2023-06-01")
    private LocalDate adoptionAvailableDate;

    @Schema(description = "공고 만료일", example = "2023-07-01")
    private LocalDate announcementExpiryDate;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "입양 요구사항", example = "넓은 공간과 정기적인 산책이 가능한 가정을 희망합니다.")
    private String adoptionRequirements;

    @ElementCollection
    @Schema(description = "이미지 URL 목록", example = "[\"https://example.com/images/happy1.jpg\", \"https://example.com/images/happy2.jpg\"]")
    private List<String> imageUrls = new ArrayList<>();

    @Schema(description = "공고 상태")
    public enum AnnouncementStatus {
        @Schema(description = "활성 상태")
        ACTIVE,
        @Schema(description = "마감 상태")
        CLOSED,
        @Schema(description = "입양 완료 상태")
        ADOPTED
    }

}