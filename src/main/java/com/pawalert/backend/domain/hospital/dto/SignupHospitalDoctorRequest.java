package com.pawalert.backend.domain.hospital.dto;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignupHospitalDoctorRequest(
        @Schema(description = "병원 이름", example = "반려동물 병원")
        String hospitalName,

        @Schema(description = "의사 인허가번호", example = "1234567890")
        String licenseNumber,

        @Schema(description = "의사 전공 예) 대형 동물 진료, 특수 동물 진료", example = "대형 동물 진료")
        String major,

        @Schema(description = "이메일 주소", example = "doctor@example.com")
        String email,

        @Schema(description = "비밀번호", example = "securePassword123")
        String password,

        @Schema(description = "병원 전화번호", example = "02-123-4567")
        String phoneNumber,

        @Schema(description = "병원 위치 (위도/경도) 및 상세주소")
        LocataionRecord locataionRecord
) {
}
