package com.pawalert.backend.domain.chat.service;

import com.pawalert.backend.domain.chat.entity.ChatRoom;
import com.pawalert.backend.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findChatRoomByUsers(String user1, String user2) {
        return chatRoomRepository.findBySenderUidAndReceiverUidOrReceiverUidAndSenderUid(user1, user2, user1, user2);
    }

    public List<ChatRoom> findChatRoomsByUser(String userId) {
        return chatRoomRepository.findBySenderUidOrReceiverUid(userId, userId);
    }
}