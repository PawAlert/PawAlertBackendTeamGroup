package com.pawalert.backend.chat;

import java.time.LocalDateTime;


public record ChatMessageDTO(
         String senderId,
         String receiverId,
         String message
) {
}
