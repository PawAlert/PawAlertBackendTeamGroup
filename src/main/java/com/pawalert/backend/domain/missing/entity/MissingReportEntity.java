package com.pawalert.backend.domain.missing.entity;

import com.pawalert.backend.domain.missing.model.MissingStatus;
import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Size(max = 255)
    @Column(name = "title", nullable = false)
    @Schema(description = "제목")
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    @Schema(description = "게시글 내용")
    private String content;

    @Column(name = "date_lost", nullable = false)
    @Schema(description = "실종날짜")
    private LocalDateTime dateLost;

    @Embedded
    @Schema(description = "실종 위치 (위도/경도)")
    private Location location;

    private String contact1;
    private String contact2;

    @Lob
    @Column(name = "description")
    @Schema(description = "특징 및 설명")
    private String description;

    @Column(name = "missing_status")
    @Enumerated(EnumType.STRING)
    @Schema(description = "상태 예: 실종 중, 발견됨, 종료")
    private MissingStatus status = MissingStatus.MISSING;

    @Column(name = "reward_amount", precision = 10, scale = 2)
    @Schema(description = "포상금액")
    private int rewardAmount;

    @ColumnDefault("'PENDING'")
    @Column(name = "reward_status")
    @Schema(description = "포상 상태, default: 대기, 지급")
    private String rewardStatus = "PENDING";

    @OneToMany(mappedBy = "missingReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "반려동물 사진 목록")
    private List<MissingReportImageEntity> missingPetImages;

    private boolean deleted = false;
}
