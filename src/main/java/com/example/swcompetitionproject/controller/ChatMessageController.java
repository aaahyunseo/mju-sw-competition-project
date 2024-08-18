package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.chatting.ChatMessageDto;
import com.example.swcompetitionproject.entity.Message;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

/**
 * WebSocket 메세지 처리 컨트롤러
 * (WebSocket 연결, 전송, 해제)
 **/

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatMessageController {
    private final SimpMessagingTemplate template;
    private final ChattingService chattingService;

    /**
     * 채팅방 입장하기
     **/
    @MessageMapping("/ws/chat/{roomId}/enter")
    public void enter(@DestinationVariable UUID roomId, @AuthenticatedUser User user) {

//        // 사용자가 처음 입장하는 경우에만 입장 메시지 전송
//        if (chattingService.isNewUserInRoom(user, roomId)) {
//            // 사용자를 채팅방에 추가
//            chattingService.addUserToRoom(user, roomId);
//            // 입장 메시지 생성 및 전송
//            ChatMessageDto enterMessageDto = new ChatMessageDto(roomId, user.getName() + "님이 입장하셨습니다.", user.getName());
//            log.info("Enter message DTO: {}", enterMessageDto);
//            template.convertAndSend("/sub/ws/chat/room/" + roomId, enterMessageDto);
//            log.info("Enter message sent.-입장 메시지 보내짐: {}",enterMessageDto.getContent());
//        }else{
//          // 기존 메시지들 전송 (처음 입장하는 유저가 아닌 경우에만)
//          log.info("기존 메세지 보내기 전 : 여기까지는 오나?");
//          List<Message> previousMessages = chattingService.getMessagesByRoomId(roomId);
//          for (Message message : previousMessages) {
//              ChatMessageDto messageDto = new ChatMessageDto(roomId, message.getContent(), message.getSender());
//              template.convertAndSend("/sub/ws/chat/room/" + roomId, messageDto);
//              log.info("Enter message sent.-기존 메시지 보내기 완료");
//          }
//        }

        // 기존 메시지들 전송 (처음 입장하는 유저가 아닌 경우에만)
        log.info("기존 메세지 보내기 전 : 여기까지는 오나?");
        List<Message> previousMessages = chattingService.getMessagesByRoomId(roomId);
        for (Message message : previousMessages) {
            ChatMessageDto messageDto = new ChatMessageDto(roomId, message.getContent(), message.getSender());
            template.convertAndSend("/sub/ws/chat/room/" + roomId, messageDto);
            log.info("Enter message sent.-기존 메시지 보내기 완료");
        }
    }

    /**
     * 채팅방에 메시지 보내기
     **/
    @MessageMapping("/ws/chat/send")
    public void message(@Payload ChatMessageDto message) {
        // MessageRepository 에 메시지 저장
        chattingService.saveMessage(message);
        log.info("message DTO: {}", message);
        // 메시지 전송
        template.convertAndSend("/sub/ws/chat/room/" + message.getRoomId(), message);
        log.info("message sent.:{}", message.getContent());
    }

    /**
     * 채팅방 퇴장하기
     **/
    @MessageMapping("/ws/chat/{roomId}/quit")
    public void quit(@DestinationVariable UUID roomId, @AuthenticatedUser User user) {
        // 사용자를 채팅방에서 제거
        chattingService.removeUserFromRoom(user, roomId);

        // 퇴장 메시지 생성 및 전송
        ChatMessageDto quitMessage = new ChatMessageDto(roomId, user.getName() + "님이 퇴장하셨습니다.", user.getName());
        template.convertAndSend("/sub/ws/chat/room/" + roomId, quitMessage);
    }
}
