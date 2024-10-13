package com.pawalert.backend.domain.chat.controller;

import com.pawalert.backend.domain.chat.entity.ChatRoom;
import com.pawalert.backend.domain.chat.repository.ChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebSocketChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketChatController(ChatRoomRepository chatRoomRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, ChatRoom.ChatMessage chatMessage) {

        try {
            // 채팅방 조회

            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));


            // 메시지 저장

            chatRoom.getMessages().add(chatMessage);
            ChatRoom savedRoom = chatRoomRepository.save(chatRoom);


            // 실시간으로 메시지 전송
            messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);
        } catch (Exception e) {
            log.error("Error processing message for room {}. Error: {}", roomId, e.getMessage(), e);
        }
    }
}