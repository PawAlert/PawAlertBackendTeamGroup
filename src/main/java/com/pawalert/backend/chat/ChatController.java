package com.pawalert.backend.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 메시지 전송 메서드
    @MessageMapping("/chat/{receiverId}")
    public void sendMessage(String senderId, String receiverId, String message) {
        // 채팅방 ID 생성
        String chatRoomId = generateChatRoomId(senderId, receiverId);

        // Redis 채널에 메시지 발행
        redisTemplate.convertAndSend("chat:" + chatRoomId, message);

        // MongoDB에 메시지 저장 (추가 로직 필요)
    }

    // 채팅방 ID 생성 메서드
    private String generateChatRoomId(String user1Id, String user2Id) {
        return user1Id.compareTo(user2Id) < 0 ? user1Id + "_" + user2Id : user2Id + "_" + user1Id;
    }
}
