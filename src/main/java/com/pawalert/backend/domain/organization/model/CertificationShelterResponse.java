package com.pawalert.backend.domain.organization.model;

public record CertificationShelterResponse(
        String shelterName,
        // 관할 구역
        String jurisdiction
) {
}
