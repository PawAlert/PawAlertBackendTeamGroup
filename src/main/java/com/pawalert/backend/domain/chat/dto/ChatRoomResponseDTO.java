//package com.pawalert.backend.domain.chat.dto;
//
//import com.pawalert.backend.domain.chat.entity.ChatRoom;
//
//import java.util.List;
//
//public record ChatRoomResponseDTO(
//        String chatRoomId,
//        List<ChatMessageDTO> messages
//) {
//    public static ChatRoomResponseDTO from(ChatRoom chatRoom) {
//        return new ChatRoomResponseDTO(
//                chatRoom.getChatRoomId(),
//                chatRoom.getMessages()
//        );
//    }
//}
