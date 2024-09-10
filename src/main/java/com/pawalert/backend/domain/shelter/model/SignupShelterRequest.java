package com.pawalert.backend.domain.shelter.model;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignupShelterRequest(
        String shelterName,
        String jurisdiction,
        @Schema(description = "보호단체 연락처")
        String contactPhone,
        @Schema(description = "로그인 이메일")
        String email,
        @Schema(description = "로그인 비밀번호")
        String password,
        LocataionRecord locataionRecord
) {
}
