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

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "address_detail1", nullable = false)
    private String addressDetail1;

    @Column(name = "address_detail2", nullable = false)
    private String addressDetail2;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
}
