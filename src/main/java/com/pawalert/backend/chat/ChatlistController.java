package com.pawalert.backend.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatlistController {

    private final ChatRoomService chatService;

    @GetMapping("/chatList")
    private ChatRoomResponseDTO chatMessageList(@RequestParam(value = "chatRoomId") String chatRoomId) {
        return chatService.getChatRoom(chatRoomId);
    }
}
