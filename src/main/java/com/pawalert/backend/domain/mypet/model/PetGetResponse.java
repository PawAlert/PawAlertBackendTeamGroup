package com.pawalert.backend.domain.mypet.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PetGetResponse(
        @Schema(description = "pet ID")
        Long petId,
        @Schema(description = "반려동물 이름")
        String petName,
        @Schema(description = "품종 : 강아지, 고양이")
        String species,
        @Schema(description = "상세품종")
        String breed,
        @Schema(description = "색상")
        String color,
        @Schema(description = "성별")
        String gender,
        @Schema(description = "마이크로칩 ID")
        String microchipId,
        @Schema(description = "중성화 여부")
        boolean neutering,
        @Schema(description = "나이")
        int age,
        @Schema(description = "사진")
        List<PetImageListRecord> petImageList
) {
}
