package com.pawalert.backend.global;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "위치 정보")
public class Location {

    @Schema(description = "우편번호", example = "03181")
    private String postcode;

    @Schema(description = "도/시", example = "서울특별시")
    private String province;

    @Schema(description = "시/군/구", example = "종로구")
    private String city;

    @Schema(description = "동/읍/면", example = "세종로")
    private String district;

    @Schema(description = "도로명", example = "세종대로 175")
    private String street;

    @Schema(description = "상세 주소", example = "광화문 앞")
    private String addressDetail;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    @Schema(description = "위도", example = "37.5665")
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    @Schema(description = "경도", example = "126.9780")
    private BigDecimal longitude;

    public static Location from(LocationRecord locationRecord) {
        return Location.builder()
                .postcode(locationRecord.postcode())
                .province(locationRecord.province())
                .city(locationRecord.city())
                .district(locationRecord.district())
                .street(locationRecord.street())
                .addressDetail(locationRecord.addressDetail())
                .latitude(locationRecord.latitude())
                .longitude(locationRecord.longitude())
                .build();
    }
}