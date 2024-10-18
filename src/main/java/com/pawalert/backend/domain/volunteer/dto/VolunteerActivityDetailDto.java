package com.pawalert.backend.domain.volunteer.dto;

import com.pawalert.backend.domain.volunteer.entity.ActivityType;
import com.pawalert.backend.domain.volunteer.entity.VolunteerActivity;
import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "봉사활동 상세 정보 DTO")
public record VolunteerActivityDetailDto(
        @Schema(description = "봉사활동 ID")
        Long id,

        @Schema(description = "사용자 UID")
        String userUid,

        @Schema(description = "봉사활동 제목")
        String title,

        @Schema(description = "봉사활동 설명")
        String description,

        @Schema(description = "봉사활동 날짜")
        LocalDate date,

        @Schema(description = "활동 유형")
        ActivityType activityType,

        @Schema(description = "시작 시간")
        LocalTime startTime,

        @Schema(description = "종료 시간")
        LocalTime endTime,

        @Schema(description = "활동 장소")
        LocationRecord location,

        @Schema(description = "필요 봉사자 수")
        int requiredVolunteers,

        @Schema(description = "필요한 기술")
        String requiredSkills,

        @Schema(description = "주최 단체명")
        String organizationName,

        @Schema(description = "담당자 이름")
        String contactName,

        @Schema(description = "담당자 이메일")
        String contactEmail,

        @Schema(description = "담당자 전화번호")
        String contactPhone,

        @Schema(description = "이미지 URL 리스트")
        List<String> images
) {
    public static VolunteerActivityDetailDto from(VolunteerActivity activity) {
        return new VolunteerActivityDetailDto(
                activity.getId(),
                activity.getUserUid(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getDate(),
                activity.getActivityType(),
                activity.getStartTime(),
                activity.getEndTime(),
                LocationRecord.getLocation(activity.getLocation()),
                activity.getRequiredVolunteers(),
                activity.getRequiredSkills(),
                activity.getOrganizationName(),
                activity.getContactName(),
                activity.getContactEmail(),
                activity.getContactPhone(),
                activity.getImages()
        );
    }
}