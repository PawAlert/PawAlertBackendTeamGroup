package com.pawalert.backend.domain.organization.entity;

import com.pawalert.backend.domain.user.entity.UserEntity;
import com.pawalert.backend.global.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class AnimalRescueRequest extends BaseEntity {
    @Id
    @Column(name = "request_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "구조 요청을 보낸 사용자의 user_id")
    private UserEntity user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "organization_id", nullable = false)
    @Schema(description = "구조 요청을 받은 동물 구조 단체의 organization_id")
    private AnimalRescueOrganization organization;

    @Lob
    @Column(name = "description")
    @Schema(description = "특징 및 설명")
    private String description;

    @ColumnDefault("'PENDING'")
    @Lob
    @Column(name = "status")
    @Schema(description = "상태 예: 신청 중, 승인됨, 거절됨")
    private String status;



}