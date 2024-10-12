package com.pawalert.backend.domain.missing.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record MissingPatchRequest(
        @Schema(description = "조회하고 싶은 게시글 ID", example = "1")
        Long missingReportId, // 실종글 ID (자동 증가)

        @Schema(description = "제목", example = "반려동물을 찾아요")
        String title, // 게시글 제목

        @Schema(description = "펫 정보 수정", example = "검은색 시바견")
        String petDescription, // 펫에 대한 추가 설명 및 특징

        @Schema(description = "연락처1", example = "010-1234-5678")
        String contact1, // 비상 연락처 1

        @Schema(description = "연락처2", example = "02-987-6543")
        String contact2, // 비상 연락처 2

        @Schema(description = "동물 종", example = "시바견")
        String petSpecies, // 펫 품종

        @Schema(description = "마이크로칩 ID", example = "123456789")
        String microchipId, // 펫 칩 ID

        @Schema(description = "특징 및 설명", example = "잘 먹고 잘 놀아요")
        String description, // 펫에 대한 특징 및 설명

        @Schema(description = "실종 상태 변경", example = "FOUND")
        String missingStatus // 실종 상태 (예: MISSING, FOUND 등)
) {
}
