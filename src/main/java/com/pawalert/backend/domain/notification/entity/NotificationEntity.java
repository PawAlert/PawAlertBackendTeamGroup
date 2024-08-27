package com.pawalert.backend.domain.notification.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notifications")
public class NotificationEntity {

    @Id
    private String id;

    private String userId;  // 알림을 받을 사용자 ID
    private String message;  // 알림 내용
    private LocalDateTime timestamp;  // 알림 생성 시간
    private boolean read;  // 알림 읽음 여부
}
