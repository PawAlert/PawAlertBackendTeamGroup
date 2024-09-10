package com.pawalert.backend.domain.shelter.model;

import com.pawalert.backend.global.ImageInfoRecord;
import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record ShelterViewResponse(
        @Schema(description = "보호센터 ID")
        Long shelterId,
        @Schema(description = "관할구역")
        String jurisdiction,
        @Schema(description = "보호센터명")
        String shelterName,
        @Schema(description = "보호센터 연락번호")
        String contactPhone,
        @Schema(description = "보호센터 위치 (위도/경도) 및 상세주소")
        LocataionRecord location,
        @Schema(description = "보호센터 website URL")
        ImageInfoRecord shelterImage,
        @Schema(description = "보호센터 email")
        String shelterEmail,
        @Schema(description = "사용자 ID")
        Long userId


) {
}
