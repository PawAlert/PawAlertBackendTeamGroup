package com.pawalert.backend.domain.notification.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
public class PushNotification {
    @Id
    @Column(name = "push_notification_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    @Schema(description = "푸시 알림을 받을 사용자의 user_id")
    private Long userId;

    @NotNull
    @Column(name = "missing_report_id", nullable = false)
    @Schema(description = "관련된 실종 신고의 missing_report_id.")
    private Long missingReportId;

    @ColumnDefault("current_timestamp()")
    @Column(name = "sent_at")
    @Schema(description = "푸시 알림을 보낸 시간")
    private Instant sentAt;

    @ColumnDefault("'SUCCESS'")
    @Lob
    @Column(name = "status")
    @Schema(description = "푸시 알림 전송 상태")
    private String status;

}