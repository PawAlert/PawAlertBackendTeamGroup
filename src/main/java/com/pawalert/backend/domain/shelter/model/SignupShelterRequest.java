package com.pawalert.backend.domain.shelter.model;

import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignupShelterRequest(
        @Schema(description = "보호센터명")
        String shelterName,
        @Schema(description = "관할구역")
        String jurisdiction,
        @Schema(description = "보호단체 연락처")
        String contactPhone,
        @Schema(description = "보호센터 website URL")
        String websiteUrl,
        @Schema(description = "로그인 이메일")
        String email,
        @Schema(description = "로그인 비밀번호")
        String password,
        @Schema(description = "보호단체 위치")
        LocationRecord location
) {
}
