package com.pawalert.backend.domain.hospital.dto;

import com.pawalert.backend.global.LocataionRecord;

public record SignupHospitalDoctorRequest(
        String hospitalName,
        String licenseNumber,
        String major,
        String email,
        String password,
        String phoneNumber,
        LocataionRecord locataionRecord
) {
}
