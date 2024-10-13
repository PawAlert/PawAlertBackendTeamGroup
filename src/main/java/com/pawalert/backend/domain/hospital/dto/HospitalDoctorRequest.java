package com.pawalert.backend.domain.hospital.dto;

import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record HospitalDoctorRequest(
        @Schema(description = "병원 전화번호", example = "02-123-4567")
        String phoneNumber,

        @Schema(description = "병원 이름", example = "반려동물 병원")
        String hospitalName,

        @Schema(description = "병원 위치 (위도/경도) 및 상세주소")
        LocationRecord detailAddress,

        @Schema(description = "의사 인허가번호", example = "1234567890")
        String licenseNumber,

        @Schema(description = "의사 전공 예) 대형 동물 진료, 특수 동물 진료", example = "대형 동물 진료")
        String major
) {
}
