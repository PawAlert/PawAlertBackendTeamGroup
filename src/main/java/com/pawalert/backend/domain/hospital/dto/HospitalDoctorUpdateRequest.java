package com.pawalert.backend.domain.hospital.dto;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record HospitalDoctorUpdateRequest(

        @Schema(description = "병원 이름")
        String hospitalName,

        @Schema(description = "병원 전화번호")
        String phoneNumber,

        @Schema(description = "병원 위치 (위도/경도) 및 상세주소")
        LocataionRecord detailAddress,
        @Schema(description = "의사 전송")
        String major

) {
}
