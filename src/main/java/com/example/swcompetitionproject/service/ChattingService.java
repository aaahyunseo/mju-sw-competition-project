package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.chatting.ChatMessageDto;
import com.example.swcompetitionproject.dto.response.chatting.ChattingRoomListData;
import com.example.swcompetitionproject.entity.*;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MessageRepository messageRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRoomRepository userRoomRepository;

    //채팅방 목록 조회
    @Transactional
    public ChattingRoomListData getRoomList(User user) {
        List<ChattingRoom> rooms = userRoomRepository.findByUser(user).stream()
                .map(UserRoom::getChattingRoom)
                .collect(Collectors.toList());

        return ChattingRoomListData.from(rooms);
    }

    //채팅방 생성하기 - 게시글 생성 시 채팅방 생성
    @Transactional
    public ChattingRoom createRoom(User user, Board board) {
        ChattingRoom room = ChattingRoom.builder()
                .title(board.getTitle())
                .manager(user.getName())
                .memberCount(1)
                .build();

        UserRoom userRoom = UserRoom.builder()
                .user(user)
                .chattingRoom(room)
                .build();
        userRoomRepository.save(userRoom);

        return chattingRoomRepository.save(room);
    }

    //채팅 메시지 저장
    @Transactional
    public Message saveMessage(User user, UUID roomId, ChatMessageDto message) {
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));

        Message newMessage = Message.builder()
                .content(message.getContent())
                .sender(user.getName())
                .chattingRoom(room)
                .build();

        return messageRepository.save(newMessage);
    }

    //사용자를 채팅방에 추가
    @Transactional
    public void addUserToRoom(User user, UUID roomId) {
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));

        RoomMember member = RoomMember.builder()
                .name(user.getName())
                .chattingRoom(room)
                .build();
        roomMemberRepository.save(member);

        room.setMemberCount(room.getMemberCount() + 1);
        chattingRoomRepository.save(room);
    }

    // 채팅방 퇴장하기
    @Transactional
    public void removeUserFromRoom(User user, UUID roomId) {
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));

        RoomMember member = roomMemberRepository.findByNameAndChattingRoom(user.getName(), room)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        roomMemberRepository.delete(member);

        room.setMemberCount(room.getMemberCount() - 1);
        chattingRoomRepository.save(room);
    }
}

