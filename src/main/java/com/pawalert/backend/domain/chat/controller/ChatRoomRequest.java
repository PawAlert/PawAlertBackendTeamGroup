package com.pawalert.backend.domain.chat.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {

    private String missingPostId;
    private String senderUid;
    private String receiverUid;


    @Override
    public String toString() {
        return "ChatRoomRequest{" +
                "missingPostId='" + missingPostId + '\'' +
                ", senderUid='" + senderUid + '\'' +
                ", receiverUid='" + receiverUid + '\'' +
                '}';
    }
}