package com.pawalert.backend.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Schema(description = "엔티티가 생성된 날짜와 시간")
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Schema(description = "엔티티가 마지막으로 수정된 날짜와 시간")
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "엔티티를 생성한 사용자 ID")
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @Schema(description = "엔티티를 마지막으로 수정한 사용자 ID")
    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;

}
