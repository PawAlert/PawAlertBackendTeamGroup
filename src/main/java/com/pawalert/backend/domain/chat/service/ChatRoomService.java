package com.pawalert.backend.domain.chat.service;

import com.pawalert.backend.domain.chat.dto.ChatMessageDTO;
import com.pawalert.backend.domain.chat.dto.ChatRoomListResponse;
import com.pawalert.backend.domain.chat.dto.ChatRoomResponseDTO;
import com.pawalert.backend.domain.chat.entity.ChatRoom;
import com.pawalert.backend.domain.chat.repository.ChatRoomRepository;
import com.pawalert.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository; // 채팅방 리포지토리
    private final UserRepository userRepository;

    // 채팅 저장
    public void addSaveMessage(String chatRoomId, ChatMessageDTO message) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByChatRoomId(chatRoomId); // Optional 사용

        ChatRoom chatRoom;

        chatRoom = optionalChatRoom.orElseGet(() -> ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .joinUserUid1(message.senderUid())
                .joinUserUid2(message.receiverUid())
                .chatRoomId(chatRoomId)
                .messages(new ArrayList<>())
                .build());
        // 새로운 메시지를 추가
        chatRoom.getMessages().add(message); // 메시지를 추가합니다.

        chatRoomRepository.save(chatRoom);
    }

    // 내 채팅 목록들 전송
    public List<ChatRoomListResponse> getFindChatRooms(String uid) {
        // 1. joinUserUid1로 검색한 결과
        List<ChatRoom> chatRoomData1 = chatRoomRepository.findByJoinUserUid1(uid);

        // 2. joinUserUid2로 검색한 결과
        List<ChatRoom> chatRoomData2 = chatRoomRepository.findByJoinUserUid2(uid);

        // 3. 두 결과를 합침
        List<ChatRoom> combinedChatRooms = new ArrayList<>(chatRoomData1);
        combinedChatRooms.addAll(chatRoomData2);

        // 4. 각 채팅방을 ChatRoomListResponse로 변환하여 리스트 반환
        return combinedChatRooms.stream()
                .map(chatRoom -> new ChatRoomListResponse(
                        chatRoom.getChatRoomId(),
                        chatRoom.getJoinUserUid1(),
                        chatRoom.getJoinUserUid2())
                )
                .toList();
    }

    // 상세 조회
    public ChatRoomResponseDTO getDetailChat(String chatRoomId) {
        // 채팅방 ID로 채팅방 데이터를 가져옴
        Optional<ChatRoom> chatRoomData = chatRoomRepository.findByChatRoomId(chatRoomId);

        // 채팅방 데이터가 존재하는 경우, ChatRoomResponseDTO로 변환
        if (chatRoomData.isPresent()) {
            ChatRoom chatRoom = chatRoomData.get();

            // ChatRoomResponseDTO로 변환하여 반환
            return new ChatRoomResponseDTO(
                    chatRoom.getChatRoomId(),
                    chatRoom.getMessages()
            );
        } else {
            // 채팅방을 찾을 수 없을 경우 예외 처리 또는 null 반환
            throw new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId);
        }
    }
}

