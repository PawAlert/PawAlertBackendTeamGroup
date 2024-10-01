package com.pawalert.backend.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate; // Redis 템플릿
    private final SimpMessagingTemplate messagingTemplate; // WebSocket을 통해 메시지를 전송하기 위한 템플릿
    private final ChatRoomService chatRoomService; // 메시지 서비스를 사용하기 위한 의존성 주입

    @MessageMapping("/chat/{receiverId}")
    public void sendMessage(ChatMessageDTO chatMessageDTO) { // ChatMessageDTO로 변경
        String senderId = chatMessageDTO.senderId();
        String receiverId = chatMessageDTO.receiverId();
        String message = chatMessageDTO.message();

        // 채팅방 ID 생성
        String chatRoomId = generateChatRoomId(senderId, receiverId);


        // Redis에 메시지 발행
        redisTemplate.convertAndSend("chat:" + chatRoomId, chatMessageDTO);

        // MongoDB에 메시지 저장
        chatRoomService.addSaveMessage(chatRoomId, chatMessageDTO);


        // WebSocket을 통해 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessageDTO);

        log.info("Message sent from " + senderId + " to " + receiverId + ": " + message);
    }

    private String generateChatRoomId(String user1Id, String user2Id) {
        return user1Id.compareTo(user2Id) < 0 ? user1Id + "_" + user2Id : user2Id + "_" + user1Id;
    }
}
