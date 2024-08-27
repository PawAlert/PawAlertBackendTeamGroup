package com.pawalert.backend.domain.organization.model;

import com.pawalert.backend.global.LocataionRecord;
import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;

public record ShelterUpdateOrCreateRequest(
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
        String websiteUrl,
        @Schema(description = "보호센터 email")
        String contactEmail
) {
}
