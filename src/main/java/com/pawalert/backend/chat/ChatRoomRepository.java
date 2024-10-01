package com.pawalert.backend.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatRoomId(String chatRoomId); // 채팅방 ID로 채팅방 찾기

}
