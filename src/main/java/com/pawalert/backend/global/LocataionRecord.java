package com.pawalert.backend.global;

import java.math.BigDecimal;

public record LocataionRecord(
        BigDecimal latitude,
        BigDecimal longitude
) {
}
