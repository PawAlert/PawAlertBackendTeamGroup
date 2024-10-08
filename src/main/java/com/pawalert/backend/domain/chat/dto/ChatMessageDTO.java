package com.pawalert.backend.domain.chat.dto;



public record ChatMessageDTO(
         String senderUid,
         String receiverUid,
         String message,
         String chatRoomId
) {
}
