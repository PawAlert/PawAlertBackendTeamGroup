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
public class Notification {
    @Id
    @Column(name = "notification_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    @Schema(description = "알림받을 사용자 ID")
    private Long userId;

    @NotNull
    @Column(name = "report_id", nullable = false)
    @Schema(description = "알림받을 신고 ID")
    private Long reportId;

    @NotNull
    @Lob
    @Column(name = "type", nullable = false)
    @Schema(description = "알림 타입")
    private String type;

    @Lob
    @Column(name = "message")
    @Schema(description = "알림 메시지")
    private String message;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("0")
    @Column(name = "is_read")
    @Schema(description = "읽음 여부")
    private Boolean isRead;

}