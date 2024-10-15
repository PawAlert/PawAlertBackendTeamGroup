package com.pawalert.backend.domain.chat.controller;

import com.pawalert.backend.domain.chat.entity.ChatRoom;
import com.pawalert.backend.domain.chat.repository.ChatRoomRepository;
import com.pawalert.backend.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOrGetChatRoom(@RequestBody ChatRoomRequest request) {
        try {
            // 기존 채팅방 조회
            ChatRoom existingRoom = chatRoomService.findChatRoomByUsers(request.getSenderUid(), request.getReceiverUid());

            if (existingRoom != null) {
                return ResponseEntity.ok(existingRoom);
            }

            // 새로운 ChatRoom 객체 생성
            LocalDateTime now = LocalDateTime.now();
            ChatRoom newChatRoom = ChatRoom.builder()
                    .id(UUID.randomUUID().toString())
                    .missingPostId(request.getMissingPostId())
                    .receiverUid(request.getReceiverUid())
                    .senderUid(request.getSenderUid())
                    .createdAt(now)
                    .build();


            // 데이터베이스에 저장
            ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);

            log.info("Chat room created: {}", savedChatRoom.getId());

            return ResponseEntity.ok(savedChatRoom);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("채팅방 생성 또는 조회에 실패했습니다.");
        }
    }


    @GetMapping("/rooms")
    public ResponseEntity<?> getChatRooms(@RequestParam String userId) {
        try {
            List<ChatRoom> chatRooms = chatRoomService.findChatRoomsByUser(userId);

            if (chatRooms.isEmpty()) {
                return ResponseEntity.ok().body(Collections.emptyList());
            }

            return ResponseEntity.ok(chatRooms);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("채팅방 목록 조회에 실패했습니다.");
        }
    }

    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<?> getChatMessages(@PathVariable String roomId) {
        try {
            return chatRoomRepository.findById(roomId)
                    .map(chatRoom -> {
                        return ResponseEntity.ok(chatRoom.getMessages());
                    })
                    .orElseGet(() -> {
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("채팅 메시지 조회에 실패했습니다.");
        }
    }
}