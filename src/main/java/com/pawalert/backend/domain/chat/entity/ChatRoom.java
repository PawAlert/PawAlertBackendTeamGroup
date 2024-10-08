package com.pawalert.backend.domain.chat.entity;

import com.pawalert.backend.domain.chat.dto.ChatMessageDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "chat_rooms")
@Builder
public class ChatRoom {
    @Id
    private String id; // 채팅방 ID
    private String chatRoomId;
    private List<ChatMessageDTO> messages = new ArrayList<>(); // 메시지 목록
    // 참여자 이메일 정보
    private String joinUserUid1;
    private String joinUserUid2;
}
