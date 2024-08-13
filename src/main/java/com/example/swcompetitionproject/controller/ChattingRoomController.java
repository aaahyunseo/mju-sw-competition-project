package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.chatting.CreateRoomDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.dto.response.chatting.ChattingRoomListData;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChattingRoomController {
    private final ChattingService chattingService;

    // 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<ResponseDto<ChattingRoomListData>> getRoomList(@AuthenticatedUser User user) {
        ChattingRoomListData chattingRoomListData = chattingService.getRoomList(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "채팅 목록 조회 완료", chattingRoomListData), HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createRoom(@AuthenticatedUser User user, @RequestBody CreateRoomDto createRoomDto) {
        chattingService.createRoom(user, createRoomDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "채팅방 생성 완료"), HttpStatus.CREATED);
    }
}
