package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.board.RoomIdDto;
import com.example.swcompetitionproject.dto.request.chatting.ChatMessageDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.dto.response.chatting.ChattingRoomListData;
import com.example.swcompetitionproject.entity.Message;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.repository.UserRepository;
import com.example.swcompetitionproject.service.ChattingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
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
    private final UserRepository userRepository;

    /**
     * 채팅방 목록 조회
     **/
    @GetMapping("/chat")
    public ResponseEntity<ResponseDto<ChattingRoomListData>> getRoomList(@AuthenticatedUser User user) {
        ChattingRoomListData chattingRoomListData = chattingService.getRoomList(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "채팅 목록 조회 완료", chattingRoomListData), HttpStatus.OK);
    }

    /**
     * 채팅방 추가하기
     **/
    @PostMapping("/chat")
    public ResponseEntity<ResponseDto<Void>> getRoomList(@AuthenticatedUser User user, @Valid @RequestBody RoomIdDto roomIdDto) {
        boolean isNewUser = chattingService.isNewUserInRoom(user.getId(), roomIdDto.getRoomId());
        if (isNewUser) {
            chattingService.addUserToRoom(user, roomIdDto.getRoomId());
        }
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "채팅방에 입장하였습니다."), HttpStatus.CREATED);
    }

    /**
     * 특정 채팅방 입장하기
     **/
    @MessageMapping("/ws/chat/{roomId}/enter")
    public void enter(@DestinationVariable UUID roomId, @Payload ChatMessageDto chatMessageDto) {
        boolean isNewUser = chattingService.isNewUserInRoom(chatMessageDto.getUserId(), chatMessageDto.getRoomId());

        // 기존 채팅방 유저일 때 기존 메시지 띄우기
        if (!isNewUser) {
            List<Message> previousMessages = chattingService.getMessagesByRoomId(roomId);
            for (Message message : previousMessages) {
                User user = userRepository.findById(message.getUserId())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
                ChatMessageDto messageDto = ChatMessageDto.builder()
                        .roomId(roomId)
                        .content(message.getContent())
                        .sender(user.getName())
                        .userId(user.getId())
                        .timestamp(message.getCreatedAt())
                        .build();
                template.convertAndSend("/sub/ws/chat/room/" + roomId, messageDto);
            }
        }

        // 새로 들어온 유저일 경우 입장 메시지 전송
        if (isNewUser) {
            chattingService.enterRoom(chatMessageDto.getUserId(), chatMessageDto.getRoomId());
            ChatMessageDto enterMessageDto = ChatMessageDto.builder()
                    .roomId(chatMessageDto.getRoomId())
                    .content(chatMessageDto.getSender() + "님이 채팅방에 입장했습니다")
                    .sender(chatMessageDto.getSender())
                    .userId(chatMessageDto.getUserId())
                    .timestamp(LocalDateTime.now())
                    .build();
            // 입장 메시지 저장 및 전송
            template.convertAndSend("/sub/ws/chat/room/" + chatMessageDto.getRoomId(), enterMessageDto);
            chattingService.saveMessage(enterMessageDto);
        }
    }

    /**
     * 채팅방에 메시지 보내기
     **/
    @MessageMapping("/ws/chat/send")
    public void message(@Payload ChatMessageDto message) {
        // MessageRepository 에 메시지 저장
        message.setTimestamp(LocalDateTime.now());
        chattingService.saveMessage(message);
        // 메시지 전송
        template.convertAndSend("/sub/ws/chat/room/" + message.getRoomId(), message);
    }

    /**
     * 채팅방 퇴장하기
     **/
    @MessageMapping("/ws/chat/{roomId}/quit")
    public void quit(@DestinationVariable UUID roomId, @Payload ChatMessageDto message) {
        // 사용자를 채팅방에서 제거
        chattingService.removeUserFromRoom(message);

        // 퇴장 메시지 생성 및 타임스탬프 설정
        ChatMessageDto quitMessage = ChatMessageDto.builder()
                .roomId(roomId)
                .content(message.getSender() + "님이 채팅방을 퇴장했습니다")
                .sender(message.getSender())
                .userId(message.getUserId())
                .timestamp(LocalDateTime.now())
                .build();

        // 톼장 메시지 저장 및 전송
        template.convertAndSend("/sub/ws/chat/room/" + message.getRoomId(), quitMessage);
        chattingService.saveMessage(quitMessage);
    }
}
