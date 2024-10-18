package com.pawalert.backend.domain.missing.entity;

import com.pawalert.backend.domain.missing.model.MissingReportRecord;
import com.pawalert.backend.domain.missing.model.MissingStatus;
import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissingReportEntity extends BaseEntity {

    @Id
    @Column(name = "missing_report_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Size(max = 255)
    @Column(name = "title", nullable = false)
    @Schema(description = "제목")
    private String MissingTitle;

    @Lob
    @Column(name = "content", nullable = false)
    @Schema(description = "실종당시 설명")
    private String incidentDescription;

    @Column(name = "date_lost", nullable = false)
    @Schema(description = "실종날짜")
    private LocalDate dateLost;

    @Embedded
    @Schema(description = "실종 위치 (위도/경도)")
    private Location location;

    private String contact1;

    private String contact2;

    @Lob
    @Column(name = "description")
    @Schema(description = "펫 상세 설명")
    private String petAdditionalInfo;

    @Column(name = "missing_status")
    @Enumerated(EnumType.STRING)
    @Schema(description = "상태 예: 실종 중, 발견됨, 종료")
    private MissingStatus status = MissingStatus.MISSING;

    @Schema(description = "펫 칩 ID", example = "123456789")
    @Column(name = "microchip_id")
    private String microchipId;

    @Schema(description = "펫 이름", example = "버디")
    @Column(name = "missing_pet_name")
    private String missingPetName;

    @Schema(description = "펫 품종", example = "골든 리트리버")
    @Column(name = "missing_species")
    private String missingSpecies;

    @Schema(description = "펫 색상", example = "황금색")
    @Column(name = "missing_pet_color")
    private String missingPetColor;

    @Schema(description = "펫 나이", example = "5")
    @Column(name = "missing_pet_age")
    private Long missingPetAge;

    @Schema(description = "펫 성별", example = "남자")
    @Column(name = "missing_pet_gender")
    private String missingPetGender;

//    @Schema(description = "중성화 여부", example = "false")
//    @Column(name = "missing_neutering")
//    private Boolean missingNeutering;

    @Schema(description = "펫 특징", example = "왼쪽 귀에 상처가 있습니다.")
    @Column(name = "missing_pet_description")
    private String missingPetDescription;

    @OneToMany(mappedBy = "missingReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "실종 동물 사진")
    private List<MissingReportImageEntity> missingPetImages;

    private boolean deleted = false;

    public static MissingReportEntity fromRequest(MissingReportRecord request, Location location, UserEntity user) {
        return MissingReportEntity.builder()
                .MissingTitle(request.missingTitle())
                .incidentDescription(request.incidentDescription())
                .dateLost(request.missingDateLost())
                .contact1(request.emergencyContact1())
                .contact2(request.emergencyContact2())
                .petAdditionalInfo(request.petAdditionalInfo())
                .status(request.status())
                .location(location)
                .microchipId(request.microchipId())
                .missingPetName(request.missingPetName())
                .missingSpecies(request.missingSpecies())
                .missingPetColor(request.missingPetColor())
                .missingPetAge(request.missingPetAge())
                .missingPetGender(request.missingPetGender())
                .missingPetDescription(request.missingPetDescription())
                .user(user)
                .build();
    }
}
