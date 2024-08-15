package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.chatting.ChatMessageDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * WebSocket 메세지 처리 컨트롤러
 * (WebSocket 연결, 전송, 해제)
 **/

@Profile("stomp")
@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final SimpMessagingTemplate template;
    private final ChattingService chattingService;

    //채팅방 입장하기
    @MessageMapping("/ws/chat/{roomId}/enter")
    public void enter(@DestinationVariable UUID roomId, @AuthenticatedUser User user, ChatMessageDto message) {
        // 사용자를 채팅방에 추가
        chattingService.addUserToRoom(user, roomId);

        // 입장 메시지 생성 및 전송
        message.setContent(user.getStudentNumber() + "님이 입장하셨습니다.");
        template.convertAndSend("/sub/ws/chat/room/" + roomId, message);
    }

    //채팅방 메세지 보내기
    @MessageMapping("/ws/chat/{roomId}/send")
    public void message(@DestinationVariable UUID roomId, @AuthenticatedUser User user, ChatMessageDto message) {
        // 메시지 저장
        chattingService.saveMessage(user, roomId, message);

        // 메시지 전송
        template.convertAndSend("/sub/ws/chat/room/" + roomId, message);
    }

    // 채팅방 퇴장하기
    @MessageMapping("/ws/chat/{roomId}/quit")
    public void quit(@DestinationVariable UUID roomId, @AuthenticatedUser User user, ChatMessageDto message) {
        // 사용자를 채팅방에서 제거
        chattingService.removeUserFromRoom(user, roomId);

        // 퇴장 메시지 생성 및 전송
        message.setContent(user.getStudentNumber() + "님이 퇴장하셨습니다.");
        template.convertAndSend("/sub/ws/chat/room/" + roomId, message);
    }
}
