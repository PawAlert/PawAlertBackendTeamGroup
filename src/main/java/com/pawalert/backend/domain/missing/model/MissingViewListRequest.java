package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

public record MissingViewListRequest(
        String address,
        String addressDetail1,
        String status

) {
}
