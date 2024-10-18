package com.pawalert.backend.global;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "위치 정보 레코드")
public record LocationRecord(
        @Schema(description = "위도", example = "37.5665")
        BigDecimal latitude,

        @Schema(description = "경도", example = "126.9780")
        BigDecimal longitude,

        @Schema(description = "우편번호", example = "03181")
        String postcode,

        @Schema(description = "도/시", example = "서울특별시")
        String province,

        @Schema(description = "시/군/구", example = "종로구")
        String city,

        @Schema(description = "동/읍/면", example = "세종로")
        String district,

        @Schema(description = "도로명", example = "세종대로 175")
        String street,

        @Schema(description = "상세 주소", example = "광화문 앞")
        String addressDetail
) {
    @Schema(description = "Location 객체로부터 LocationRecord 생성")
    public static LocationRecord getLocation(Location location) {
        return new LocationRecord(
                location.getLatitude(),
                location.getLongitude(),
                location.getPostcode(),
                location.getProvince(),
                location.getCity(),
                location.getDistrict(),
                location.getStreet(),
                location.getAddressDetail()
        );
    }
}