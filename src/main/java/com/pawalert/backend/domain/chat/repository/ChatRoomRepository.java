package com.pawalert.backend.domain.chat.repository;

import com.pawalert.backend.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatRoomId(String chatRoomId); // 채팅방 ID로 채팅방 찾기

    //joinUserEmail1, joinUserEmail2 로 찾기
    List<ChatRoom> findByJoinUserUid1(String joinUserEmail1);

    List<ChatRoom> findByJoinUserUid2(String joinUserEmail2);

}


//private String joinUserEmail1;
//private String joinUserEmail2;
//}