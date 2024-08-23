package com.pawalert.backend;

import com.pawalert.backend.domain.mypet.entity.PetEntity;
import com.pawalert.backend.global.BaseEntity;
import com.pawalert.backend.global.Location;
import com.pawalert.backend.domain.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class MissingReport extends BaseEntity {
    @Id
    @Column(name = "missing_report_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotNull
    @Column(name = "date_lost", nullable = false)
    @Schema(description = "실종날짜")
    private LocalDate dateLost;

    @Schema(description = "실종 위도/경도")
    @Embedded
    private Location location;

    @Lob
    @Column(name = "description")
    @Schema(description = "특징 및 설명")
    private String description;

    @ColumnDefault("'LOST'")
    @Lob
    @Column(name = "status")
    @Schema(description = "상태 예: 실종 중, 발견됨, 종료")
    private String status;

    @Column(name = "reward_amount", precision = 10, scale = 2)
    @Schema(description = "포상금액")
    private BigDecimal rewardAmount;

    @Size(max = 10)
    @Column(name = "reward_currency", length = 10)
    @Schema(description = "포상 통화, default: KRW")
    private String rewardCurrency;

    @ColumnDefault("'PENDING'")
    @Lob
    @Column(name = "reward_status")
    @Schema(description = "포상 상태, default: 대기, 지급")
    private String rewardStatus;



}