package com.pawalert.backend.domain.mypet.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PetGetResponse(
        @Schema(description = "반려동물 ID", example = "1")
        Long petId,

        @Schema(description = "반려동물 이름", example = "바둑이")
        String petName,

        @Schema(description = "품종", example = "강아지, 고양이")
        String species,

        @Schema(description = "색상", example = "흰색")
        String color,

        @Schema(description = "성별", example = "수컷")
        String gender,

        @Schema(description = "마이크로칩 ID", example = "ABC123456")
        String microchipId,

        @Schema(description = "중성화 여부", example = "true")
        boolean neutering,

        @Schema(description = "나이", example = "5")
        int age,

        @Schema(description = "사진 리스트")
        List<PetImageListRecord> petImageList
) {
}
