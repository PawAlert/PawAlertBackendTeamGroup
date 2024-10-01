package com.pawalert.backend.chat;

import java.util.List;

public record ChatRoomResponseDTO(
        String chatRoomId,
        List<ChatMessageDTO> messages
) {}
