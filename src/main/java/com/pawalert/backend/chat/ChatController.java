package com.pawalert.backend.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {


    private final RedisTemplate<String, Object> redisTemplate;

    private final ChatMessageService chatMessageService;

    // 클라이언트가 "/app/chat" 경로로 보낸 메시지를 처리하고 저장
    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message) {
        // Redis 채널에 메시지 발행
        redisTemplate.convertAndSend("chat", message);

        // MongoDB에 메시지 저장
        chatMessageService.saveMessage(message);

        System.out.println("Message sent to Redis and saved to MongoDB: " + message.getMessage());
    }
}
