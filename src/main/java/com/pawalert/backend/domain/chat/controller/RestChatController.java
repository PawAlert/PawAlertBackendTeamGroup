package com.pawalert.backend.domain.chat.controller;

import com.pawalert.backend.domain.chat.dto.ChatRoomListResponse;
import com.pawalert.backend.domain.chat.dto.ChatRoomResponseDTO;
import com.pawalert.backend.domain.chat.service.ChatRoomService;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class RestChatController {

    private final ChatRoomService chatService;


    // 내 채팅 목록들 조회
    @GetMapping("/chatDetailList")
    public List<ChatRoomListResponse> chatDetailList(@AuthenticationPrincipal CustomUserDetails user) {
        return chatService.getFindChatRooms(user.getUid());
    }

    // 내 채팅 상세 가져오기
    @GetMapping("/getDetailChat")
    public ChatRoomResponseDTO getDetailChat(@RequestParam("chatRoomId") String chatRoomId) {
        return chatService.getDetailChat(chatRoomId);
    }

}
