//package com.pawalert.backend.domain.notification.service;
//
//import com.pawalert.backend.domain.notification.entity.NotificationEntity;
//import com.pawalert.backend.domain.notification.repository.NotificationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//@RequiredArgsConstructor
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final ChannelTopic topic;
//    private final SimpMessagingTemplate messagingTemplate;  // WebSocket을 사용하기 위한 템플릿
//
//    public void sendNotification(String userId, String message) {
//        // 1. 알림 저장 (MongoDB에 저장)
//        NotificationEntity notification = NotificationEntity.builder()
//                .userId(userId)
//                .message(message)
//                .timestamp(LocalDateTime.now())
//                .read(false)
//                .build();
//        notificationRepository.save(notification);
//
//        // 2. 실시간 알림 전송 (Redis Pub/Sub)
//        redisTemplate.convertAndSend(topic.getTopic(), message);
//
//        // 3. WebSocket을 통해 실시간 알림 전송
//        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
//    }
//}
