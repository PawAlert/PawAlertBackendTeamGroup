package com.pawalert.backend.domain.volunteer.dto;

import com.pawalert.backend.domain.volunteer.entity.ActivityType;
import com.pawalert.backend.global.LocationRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "봉사활동 생성 요청 DTO")
public record VolunteerActivityRequest(
        @Schema(description = "봉사활동 제목", example = "지역 공원 청소")
        String title,

        @Schema(description = "봉사활동 설명", example = "우리 동네 공원을 깨끗이 합시다.")
        String description,

        @Schema(description = "봉사활동 날짜", example = "2023-06-15")
        LocalDate date,

        @Schema(description = "활동 유형", example = "CARE")
        ActivityType activityType,

        @Schema(description = "시작 시간", example = "09:00")
        LocalTime startTime,

        @Schema(description = "종료 시간", example = "12:00")
        LocalTime endTime,

        @Schema(description = "활동 장소")
        LocationRecord location,

        @Schema(description = "필요 봉사자 수", example = "10")
        int requiredVolunteers,

        @Schema(description = "필요한 기술", example = "청소도구 사용 가능자")
        String requiredSkills,

        @Schema(description = "주최 단체명", example = "푸른 도시 만들기")
        String organizationName,

        @Schema(description = "담당자 이름", example = "김철수")
        String contactName,

        @Schema(description = "담당자 이메일", example = "kim@example.com")
        String contactEmail,

        @Schema(description = "담당자 전화번호", example = "010-1234-5678")
        String contactPhone,

        @Schema(description = "이미지 URL 리스트")
        List<String> images
) {}