package com.pawalert.backend.global;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public record LocataionRecord(
        BigDecimal latitude,
        BigDecimal longitude,
        String postcode,
        String address,
        String addressDetail1,
        String extraAddress
) {
}