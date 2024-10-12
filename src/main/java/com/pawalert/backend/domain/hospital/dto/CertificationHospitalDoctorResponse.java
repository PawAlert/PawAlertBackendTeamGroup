package com.pawalert.backend.domain.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CertificationHospitalDoctorResponse(
        @Schema(description = "의사 라이센스 번호", example = "123456789")
        String licenseNumber,

        @Schema(description = "병원 이름", example = "서울 동물병원")
        String hospitalName
) {
}
