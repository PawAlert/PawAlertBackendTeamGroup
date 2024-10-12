package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.global.LocataionRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record MissingReportRecord(
        @Schema(description = "제목", example = "반려동물을 찾아요")
        String title,

        @Schema(description = "실종 당시 상황 설명", example = "산책 중에 잃어버렸습니다.")
        String incidentDescription,

        @Schema(description = "실종 날짜", example = "2024-12-31")
        LocalDate dateLost,

        @Schema(description = "비상 연락처1", example = "010-1234-5678")
        String emergencyContact1,

        @Schema(description = "비상 연락처2", example = "010-8765-4321")
        String emergencyContact2,

        @Schema(description = "펫에 대한 추가 설명 및 특징", example = "사람을 좋아하고 착합니다.")
        String petAdditionalInfo,

        @Schema(description = "상태", example = "MISSING",
                allowableValues = {"MISSING", "FOUND", "COMPLETED"})
        MissingStatus status,

        @Schema(description = "펫 칩 ID", example = "123456789")
        String microchipId,

        @Schema(description = "펫 이름", example = "버디")
        String petName,

        @Schema(description = "펫 품종", example = "골든 리트리버")
        String species,

        @Schema(description = "펫 색상", example = "황금색")
        String petColor,

        @Schema(description = "펫 나이", example = "5")
        int age,

        @Schema(description = "펫 성별", example = "남자")
        String petGender,

        @Schema(description = "펫 특징", example = "왼쪽 귀에 상처가 있습니다.")
        String petDescription,

        @Schema(description = "실종 당시 위치 (위도/경도)")
        LocataionRecord locationRecord // 오타 수정
) {
}
