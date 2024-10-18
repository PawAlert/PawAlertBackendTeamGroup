package com.pawalert.backend.domain.volunteer.entity;

import com.pawalert.backend.global.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "봉사활동 엔티티")
public class VolunteerActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "봉사활동 ID")
    private Long id;

    @Schema(description = "사용자 UID")
    private String userUid;

    @Schema(description = "봉사활동 제목")
    private String title;

    @Schema(description = "봉사활동 설명")
    private String description;

    @Schema(description = "봉사활동 날짜")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Schema(description = "활동 유형")
    @Column(name = "activity_type", length = 20)
    private ActivityType activityType;

    @Schema(description = "시작 시간")
    private LocalTime startTime;

    @Schema(description = "종료 시간")
    private LocalTime endTime;

    @Embedded
    @Schema(description = "활동 장소")
    private Location location;

    @Schema(description = "필요 봉사자 수")
    private int requiredVolunteers;

    @Schema(description = "필요한 기술")
    private String requiredSkills;

    @Schema(description = "주최 단체명")
    private String organizationName;

    @Schema(description = "담당자 이름")
    private String contactName;

    @Schema(description = "담당자 이메일")
    private String contactEmail;

    @Schema(description = "담당자 전화번호")
    private String contactPhone;

    @ElementCollection
    @Schema(description = "이미지 URL 리스트")
    private List<String> images;
}

