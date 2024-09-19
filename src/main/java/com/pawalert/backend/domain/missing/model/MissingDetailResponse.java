package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.domain.mypet.model.PetImageListRecord;
import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MissingDetailResponse(
        // 작성자 정보
        @Schema(description = "작성자 ID")
        String userName,
        @Schema(description = "작성자 전화번호")
        String phoneNumber,
        @Schema(description = "자신이 작성했는지")
        boolean isMine,


        // 글 정보
        @Schema(description = "실종글 Id")
        Long missingReportId,
        @Schema(description = "제목")
        String title,
        @Schema(description = "게시글 내용")
        String content,
        @Schema(description = "실종날짜 2024-12-31T23:59:59")
        LocalDate dateLost,
        @Schema(description = "실종 위치 위도/경도")
        Location location,
        @Schema(description = "특징 및 설명")
        String description,
        @Schema(description = "상태 예: 실종 중, 발견됨, 종료 = 기본값 MISSING")
        String missingStatus,

        //펫정보
        @Schema(description = "펫 이름")
        String petName,
        @Schema(description = "펫 품종")
        String petSpecies,
        @Schema(description = "펫 중성화 여부")
        boolean neutering,
        @Schema(description = "펫 색상")
        String color,
        @Schema(description = "펫 나이")
        int age,
        @Schema(description = "펫 성별")
        String gender,
        @Schema(description = "펫 칩 Id")
        String microchipId,
        @Schema(description = "펫 특징")
        String petDescription,
        @Schema(description = "펫 사진 URL")
        List<PetImageListRecord> missingPetImages,
        @Schema(description = "연락처1")
        String contact1,
        @Schema(description = "연락처2")
        String contact2
        ) {
}
