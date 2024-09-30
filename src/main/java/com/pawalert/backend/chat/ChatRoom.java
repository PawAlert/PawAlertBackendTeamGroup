package com.pawalert.backend.chat;

import java.util.List;

public class ChatRoom {
    private String id;         // 채팅방 ID (UID1_UID2 형태)
    private String user1Id;   // 첫 번째 사용자 UID
    private String user2Id;   // 두 번째 사용자 UID
    private List<ChatMessage> messages; // 해당 채팅방의 메시지 기록

    // 생성자, getter, setter 등 추가
}
