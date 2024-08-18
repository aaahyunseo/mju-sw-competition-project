package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.board.RoomIdDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.dto.response.chatting.ChattingRoomListData;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.ChattingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChattingRoomController {
    private final ChattingService chattingService;

    /**
     * 채팅방 목록 조회
     **/
    @GetMapping
    public ResponseEntity<ResponseDto<ChattingRoomListData>> getRoomList(@AuthenticatedUser User user) {
        ChattingRoomListData chattingRoomListData = chattingService.getRoomList(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "채팅 목록 조회 완료", chattingRoomListData), HttpStatus.OK);
    }

    /**
     * 다른 유저의 채팅방 추가하기
     **/
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> getRoomList(@AuthenticatedUser User user, @Valid @RequestBody RoomIdDto roomIdDto) {
        chattingService.addUserToRoom(user, roomIdDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "채팅방 추가 완료"), HttpStatus.OK);
    }
}
