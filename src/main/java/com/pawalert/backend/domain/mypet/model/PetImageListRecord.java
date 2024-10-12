package com.pawalert.backend.domain.mypet.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record PetImageListRecord(
        @Schema(description = "반려동물 ID", example = "1")
        Long petId,

        @Schema(description = "이미지 URL", example = "http://example.com/image.jpg")
        String imageUrl
) {
}
