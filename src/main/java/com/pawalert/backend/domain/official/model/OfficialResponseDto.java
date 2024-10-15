package com.pawalert.backend.domain.official.model;

import com.pawalert.backend.global.Location;

public record OfficialResponseDto(
        String institutionName,
        String representativeName,
        String email,
        String phoneNumber,
        String institutionType,
        Location location,
        String website,
        String institutionDescription,
        String operatingHours,
        String status
) {}