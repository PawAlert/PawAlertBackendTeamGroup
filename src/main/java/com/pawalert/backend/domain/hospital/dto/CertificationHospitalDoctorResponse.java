package com.pawalert.backend.domain.hospital.dto;

public record CertificationHospitalDoctorResponse(
        //라이센스
        String licenseNumber,
        //병원이름
        String hospitalName
) {
}
