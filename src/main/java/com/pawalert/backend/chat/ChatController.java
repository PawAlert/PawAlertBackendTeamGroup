package com.pawalert.backend.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{receiverId}")
    public void sendMessage(ChatMessage chatMessage) {
        String senderId = chatMessage.getSenderId();
        String receiverId = chatMessage.getReceiverId();
        String message = chatMessage.getMessage();

        // 채팅방 ID 생성
        String chatRoomId = generateChatRoomId(senderId, receiverId);

        // Redis에 메시지 발행
        redisTemplate.convertAndSend("chat:" + chatRoomId, message);

        // WebSocket을 통해 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);

        log.info("Message sent from " + senderId + " to " + receiverId + ": " + message);
    }

    private String generateChatRoomId(String user1Id, String user2Id) {
        return user1Id.compareTo(user2Id) < 0 ? user1Id + "_" + user2Id : user2Id + "_" + user1Id;
    }
}