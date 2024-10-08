package com.pawalert.backend.domain.chat.dto;

public record ChatRoomListResponse(
        String chatRoomId,
        String userUid1,
        String userUid2
) {

}
