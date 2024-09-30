package com.pawalert.backend.chat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "chatMessages")
public class ChatMessage {

    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private String message;
    private LocalDateTime timestamp;

}
