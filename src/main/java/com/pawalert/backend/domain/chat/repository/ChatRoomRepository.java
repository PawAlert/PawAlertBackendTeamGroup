package com.pawalert.backend.domain.chat.repository;

import com.pawalert.backend.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    ChatRoom findBySenderUidAndReceiverUidOrReceiverUidAndSenderUid(String user1, String user2, String user3, String user4);
    List<ChatRoom> findBySenderUidOrReceiverUid(String user1, String user2);
}