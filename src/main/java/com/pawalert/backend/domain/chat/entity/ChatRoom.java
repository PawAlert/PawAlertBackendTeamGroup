package com.pawalert.backend.domain.chat.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatrooms")
public class ChatRoom {

    @Id
    private String id;

    private String missingPostId;
    private String senderUid;
    private String receiverUid;
    private LocalDateTime createdAt;
    private List<ChatMessage> messages = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        private String chatRoomId;
        private String senderUid;
        private String receiverUid;
        private String message;
        private long timestamp;

    }
}