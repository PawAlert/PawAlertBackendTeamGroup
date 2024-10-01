package com.pawalert.backend.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository; // 채팅방 리포지토리

    public void addSaveMessage(String chatRoomId, ChatMessageDTO message) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByChatRoomId(chatRoomId); // Optional 사용

        ChatRoom chatRoom;

        chatRoom = optionalChatRoom.orElseGet(() -> ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .messages(new ArrayList<ChatMessageDTO>())
                .build());

        // 새로운 메시지를 추가
        chatRoom.getMessages().add(message); // 메시지를 추가합니다.

        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 조회
    public ChatRoomResponseDTO getChatRoom(String chatRoomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId);
        if (chatRoom.isPresent()) {
            List<ChatMessageDTO> messages = chatRoom.get().getMessages().stream()
                    .map(message -> new ChatMessageDTO(
                            message.senderId(),
                            message.receiverId(),
                            message.message()))
                    .toList();
            return new ChatRoomResponseDTO(chatRoomId, messages);
        }
        return null; // 채팅방이 없을 경우 null 반환
    }
}

