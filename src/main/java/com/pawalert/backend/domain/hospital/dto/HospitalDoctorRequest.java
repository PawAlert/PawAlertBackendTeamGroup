        package com.pawalert.backend.domain.hospital.dto;

        import com.pawalert.backend.global.LocataionRecord;
        import com.pawalert.backend.global.Location;
        import io.swagger.v3.oas.annotations.media.Schema;

        public record HospitalDoctorRequest(
                @Schema(description = "병원 전화번호")
                String phoneNumber,
                @Schema(description = "병원 이름")
                String hospitalName,
                @Schema(description = "병원 위치 (위도/경도) 및 상세주소")
                LocataionRecord detailAddress,
                @Schema(description = "의사 인허가번호")
                String licenseNumber,
                @Schema(description = "의사 전공 예) 대형 동물 진료, 특수 동물 진료")
                String major

        ) {
        }
