package com.pawalert.backend.domain.mypet.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PetUpdateRequest(
        @Schema(description = "수정할 pet ID", example = "1")
        Long petId,

        @Schema(description = "반려동물 이름", example = "바둑이")
        String petName,

        @Schema(description = "품종 : 강아지, 고양이", example = "고양이")
        String species,

        @Schema(description = "상세품종", example = "페르시안")
        String breed,

        @Schema(description = "색상", example = "회색")
        String color,

        @Schema(description = "성별", example = "암컷")
        String gender,

        @Schema(description = "마이크로칩 ID", example = "987654321")
        String microchipId,

        @Schema(description = "중성화 여부", example = "false")
        boolean neutering,

        @Schema(description = "나이", example = "5")
        int age,

        @Schema(description = "삭제할 사진 ID 목록", example = "[1, 2, 3]")
        List<Long> deletePhotoIds
) {
}
