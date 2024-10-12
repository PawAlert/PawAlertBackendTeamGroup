package com.pawalert.backend.global;

import jakarta.persistence.Column;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record LocataionRecord(
        @Schema(description = "위도", example = "37.5665")
        BigDecimal latitude,

        @Schema(description = "경도", example = "126.978")
        BigDecimal longitude,

        @Schema(description = "우편번호", example = "04524")
        String postcode,

        @Schema(description = "주소", example = "서울특별시 중구 세종대로 110")
        String address,

        @Schema(description = "상세 주소", example = "1층 101호")
        String addressDetail
) {
    public static LocataionRecord getLocation(Location location) {
        return new LocataionRecord(
                location.getLatitude(),
                location.getLongitude(),
                location.getPostcode(),
                location.getAddress(),
                location.getAddressDetail()
        );
    }
}
