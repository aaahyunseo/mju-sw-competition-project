package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.chatting.ChatRequestDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    @MessageMapping("/ws/chat/enter")
    // /pub/ws/chat/enter 로 동작
    // -> pub(전송) 은 prefix 인데 config 에서 /pub 이 등록되어 있어서 경로가 합쳐짐.
    // message(roomId, content)는 JSON 형식으로 받아와짐.
    public void enter(@AuthenticatedUser User user, ChatRequestDto message) {
        // 사용자를 채팅방에 추가
        chattingService.addUserToRoom(user, message);

        // 입장 메시지 생성 및 전송
        message.setContent(user.getName() + "님이 입장하셨습니다.");
        template.convertAndSend("/sub/ws/chat/room/" + message.getRoomId(), message);
    }

    //채팅방 메세지 보내기
    @MessageMapping("/ws/chat/send")
    public void message(@AuthenticatedUser User user, ChatRequestDto message) {
        // 메시지 저장
        chattingService.saveMessage(user, message);

        // 메시지 전송
        // /room/{roomId}를 구독하는 모든 유저에게 메시지 전송.
        template.convertAndSend("/sub/ws/chat/room/" + message.getRoomId(), message);
    }

    // 채팅방 퇴장하기
    @MessageMapping("/ws/chat/quit")
    public void quit(@AuthenticatedUser User user, ChatRequestDto message) {
        // 사용자를 채팅방에서 제거
        chattingService.removeUserFromRoom(user, message);

        // 퇴장 메시지 생성 및 전송
        message.setContent(user.getName() + "님이 퇴장하셨습니다.");
        template.convertAndSend("/sub/ws/chat/room/" + message.getRoomId(), message);
    }
}
