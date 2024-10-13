package com.pawalert.backend.domain.missing.model;

import com.pawalert.backend.domain.comment.dto.CommentResponse;
import com.pawalert.backend.domain.missing.entity.MissingReportEntity;
import com.pawalert.backend.domain.mypet.model.PetImageListRecord;
import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record MissingDetailResponse(
        @Schema(description = "작성자 ID", example = "user123")
        String userName,

        @Schema(description = "사용자 UID", example = "uid123")
        String userUid,

        @Schema(description = "작성자 전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "자신이 작성했는지", example = "true")
        boolean isMine,

        @Schema(description = "실종글 Id", example = "1")
        Long missingReportId,

        @Schema(description = "제목", example = "반려동물을 찾아요")
        String title,

        @Schema(description = "게시글 내용", example = "우리 강아지가 실종되었습니다.")
        String content,

        @Schema(description = "실종날짜", example = "2024-12-31")
        LocalDate dateLost,

        @Schema(description = "실종 위치 위도/경도")
        Location location,

        @Schema(description = "특징 및 설명", example = "검정색 털과 흰색 발")
        String description,

        @Schema(description = "상태 예: 실종 중, 발견됨, 종료 = 기본값 MISSING", example = "MISSING")
        String missingStatus,

        @Schema(description = "펫 이름", example = "바둑이")
        String petName,

        @Schema(description = "펫 품종", example = "시츄")
        String petSpecies,

        @Schema(description = "펫 중성화 여부", example = "false")
        boolean neutering,

        @Schema(description = "펫 색상", example = "검정색")
        String color,

        @Schema(description = "펫 나이", example = "5")
        Long age,

        @Schema(description = "펫 성별", example = "수컷")
        String gender,

        @Schema(description = "펫 칩 Id", example = "chip12345")
        String microchipId,

        @Schema(description = "펫 특징", example = "귀여운 눈")
        String petDescription,

        @Schema(description = "펫 사진 URL")
        List<PetImageListRecord> missingPetImages,

        @Schema(description = "연락처1", example = "010-9876-5432")
        String contact1,

        @Schema(description = "연락처2", example = "010-5678-1234")
        String contact2
) {
    public static MissingDetailResponse from(MissingReportEntity missingReport, boolean isMine) {
        return new MissingDetailResponse(
                missingReport.getUser().getUserName(),
                missingReport.getUser().getUid(),
                missingReport.getUser().getPhoneNumber(),
                isMine,
                missingReport.getId(),
                missingReport.getMissingTitle(),
                // 실종 당시 설명
                missingReport.getIncidentDescription(),
                missingReport.getDateLost(),
                missingReport.getLocation(),
                //실종동물 설명
                missingReport.getPetAdditionalInfo(),
                missingReport.getStatus().toString(),
                missingReport.getMissingPetName(),
                missingReport.getMissingSpecies(),
                missingReport.getMissingNeutering(),
                missingReport.getMissingPetColor(),
                missingReport.getMissingPetAge(),
                missingReport.getMissingPetGender(),
                missingReport.getMicrochipId(),
                missingReport.getMissingPetDescription(),
                missingReport.getMissingPetImages().stream()
                        .map(image -> new PetImageListRecord(image.getId(), image.getMissingPhotoUrl()))
                        .toList(),
                missingReport.getContact1(),
                missingReport.getContact2()
        );
    }
}
