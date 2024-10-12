package com.pawalert.backend.domain.shelter.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record CertificationShelterResponse(
        @Schema(description = "보호센터명", example = "반려동물 보호센터")
        String shelterName,

        @Schema(description = "관할 구역", example = "서울특별시")
        String jurisdiction
) {
}
