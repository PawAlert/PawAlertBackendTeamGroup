package com.pawalert.backend.domain.shelter.model;

import com.pawalert.backend.domain.shelter.entity.AnimalRescueOrganizationEntity;
import com.pawalert.backend.global.LocataionRecord;
import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;

public record ShelterJoinDto(
        @Schema(description = "이메일 / 비회원 가입의 경우에만")
        String email,
        @Schema(description = "비밀번호 / 비회원 가입의 경우에만")
        String password,
        @Schema(description = "관할구역")
        String jurisdiction,
        @Schema(description = "보호센터명")
        String shelterName,
        @Schema(description = "보호센터 연락번호")
        String contactPhone,
        @Schema(description = "보호센터 위치 (위도/경도) 및 상세주소")
        LocataionRecord location,
        @Schema(description = "보호센터 website URL")
        String websiteUrl,
        @Schema(description = "보호센터 email")
        String contactEmail
) {
        public  AnimalRescueOrganizationEntity toEntity(Long userId, String basicImage) {
                return AnimalRescueOrganizationEntity.builder()
                        .shelterName(shelterName)
                        .jurisdiction(jurisdiction)
                        .contactPhone(contactPhone)
                        .contactEmail(contactEmail)
                        .websiteUrl(websiteUrl)
                        .location(Location.from(location))
                        .shelterProfileImage(basicImage)
                        .userId(userId)
                        .build();
        }
}