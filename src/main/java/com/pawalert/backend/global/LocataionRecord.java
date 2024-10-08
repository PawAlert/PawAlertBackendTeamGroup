package com.pawalert.backend.global;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public record LocataionRecord(
        BigDecimal latitude,
        BigDecimal longitude,
        String postcode,
        String address,
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