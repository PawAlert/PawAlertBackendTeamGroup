package com.pawalert.backend.global;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    // 우편번호
    private String postcode;

    // 서울특별시 강남구
    private String address;
    //     상세주소
    private String addressDetail;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    public static Location from(LocationRecord locataionRecord) {
        Location location = new Location();
        location.postcode = locataionRecord.postcode();
        location.address = locataionRecord.address();
        location.addressDetail = locataionRecord.addressDetail();
        location.latitude = locataionRecord.latitude();
        location.longitude = locataionRecord.longitude();
        return location;
    }

}
