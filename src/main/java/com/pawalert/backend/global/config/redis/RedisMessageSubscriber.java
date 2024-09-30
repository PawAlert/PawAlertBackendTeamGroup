package com.pawalert.backend.global.config.redis;

import com.pawalert.backend.chat.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private ObjectMapper objectMapper = new ObjectMapper();

    // 수신한 메서드 처리 역할
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // Redis에서 수신한 메시지를 처리 (예: ChatMessage로 변환)
            String messageBody = new String(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(messageBody, ChatMessage.class);

            // 메시지 처리 로직 (예: 로그 출력)
            System.out.println("Received message: " + chatMessage.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
