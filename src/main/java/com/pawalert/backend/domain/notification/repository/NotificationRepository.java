package com.pawalert.backend.domain.notification.repository;

import com.pawalert.backend.domain.notification.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    List<NotificationEntity> findByUserIdAndReadFalse(String userId);  // 읽지 않은 알림 조회
}
