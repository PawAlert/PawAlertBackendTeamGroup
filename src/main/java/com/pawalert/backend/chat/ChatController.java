package com.pawalert.backend.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 메시지 전송 메서드
    @MessageMapping("/chat/{receiverId}")
    public void sendMessage(ChatMessage chatMessage) {
        // chatMessage는 senderId와 message를 포함해야 함
        String senderId = chatMessage.getSenderId();
        String message = chatMessage.getMessage();

        // 메시지를 Redis에 발행
        redisTemplate.convertAndSend("chat:" + chatMessage.getReceiverId(), message);

        // 메시지 로그 출력
        log.info("Message sent from " + senderId + " to " + chatMessage.getReceiverId() + ": " + message);
    }

    // 채팅방 ID 생성 메서드
    private String generateChatRoomId(String user1Id, String user2Id) {
        return user1Id.compareTo(user2Id) < 0 ? user1Id + "_" + user2Id : user2Id + "_" + user1Id;
    }
}
