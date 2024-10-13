package com.pawalert.backend.domain.hospital.dto;

import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record HospitalDoctorViewResponse(
        @Schema(description = "병원 ID", example = "1")
        Long hospitalId,

        @Schema(description = "병원 이름", example = "하나동물병원")
        String hospitalName,

        @Schema(description = "병원 전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "의사 인허가번호", example = "D123456789")
        String licenseNumber,

        @Schema(description = "의사 전공 예) 대형 동물 진료, 특수 동물 진료", example = "대형 동물 진료")
        String major,

        @Schema(description = "병원 이미지 정보", example = "http://example.com/hospital_image.jpg")
        String hospitalImage,

        @Schema(description = "병원 위치 (위도/경도) 및 상세주소")
        LocationRecord detailAddress,

        @Schema(description = "사용자 ID", example = "user456")
        Long userId
) {
}
