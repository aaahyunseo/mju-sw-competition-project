package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.chatting.ChatMessageDto;
import com.example.swcompetitionproject.dto.response.chatting.ChattingRoomListData;
import com.example.swcompetitionproject.entity.*;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.ForbiddenException;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    /**
     * 채팅방 목록 조회
     **/
    @Transactional
    public ChattingRoomListData getRoomList(User user) {
        List<ChattingRoom> rooms = userRoomRepository.findByUser(user).stream()
                .map(UserRoom::getChattingRoom)
                .sorted(Comparator.comparing(ChattingRoom::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return ChattingRoomListData.from(rooms);
    }

    /**
     * 채팅방 생성하기 - 게시글 작성 시에 채팅방 생성
     **/
    @Transactional
    public ChattingRoom createRoom(User user, Board board) {
        ChattingRoom room = ChattingRoom.builder()
                .title(board.getTitle())
                .manager(user.getName())
                .memberCount(1)
                .board(board)
                .build();

        UserRoom userRoom = UserRoom.builder()
                .user(user)
                .chattingRoom(room)
                .build();
        userRoomRepository.save(userRoom);

        return chattingRoomRepository.save(room);
    }

    /**
     * 채팅 메세지 저장하기
     **/
    @Transactional
    public Message saveMessage(ChatMessageDto message) {
        ChattingRoom room = chattingRoomRepository.findById(message.getRoomId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));

        Message newMessage = Message.builder()
                .content(message.getContent())
                .sender(message.getSender())
                .chattingRoom(room)
                .build();

        return messageRepository.save(newMessage);
    }

    /**
     * 채팅방에 유저 추가하기
     **/
    @Transactional
    public void addUserToRoom(User user, UUID roomId) {
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));

        // 유저가 이미 채팅방에 있는지 확인
        boolean isUserInRoom = userRoomRepository.existsByUserAndChattingRoom(user, room);
        if (isUserInRoom) {
            throw new UnauthorizedException(ErrorCode.USER_ALREADY_IN_ROOM);
        }
        // 채팅방의 현재 인원 수와 최대 인원 수 비교
        if (room.getMemberCount() >= room.getBoard().getTotal()) {
            throw new UnauthorizedException(ErrorCode.ROOM_FULL);
        }
        // 다른 성별일 경우
        if (room.getBoard().getUser().getGender() != user.getGender()) {
            throw new UnauthorizedException(ErrorCode.FORBIDDEN_GENDER_ROOM);
        }

        room.setMemberCount(room.getMemberCount() + 1);
        chattingRoomRepository.save(room);
    }

    /**
     * 채팅방 들어가기
     **/
    @Transactional
    public void enterRoom(String name, UUID roomId) {
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        UserRoom userRoom = UserRoom.builder()
                .user(user)
                .chattingRoom(room)
                .build();
        userRoomRepository.save(userRoom);
    }


    /**
     * 채팅방 퇴장하기
     **/
    @Transactional
    public void removeUserFromRoom(ChatMessageDto chatMessageDto) {
        ChattingRoom room = chattingRoomRepository.findById(chatMessageDto.getRoomId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));

        User user = userRepository.findByName(chatMessageDto.getSender())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        UserRoom userRoom = userRoomRepository.findByUserAndChattingRoom(user, room)
                .orElseThrow(() -> new ForbiddenException(ErrorCode.NO_ACCESS));

        userRoomRepository.delete(userRoom);

        room.setMemberCount(room.getMemberCount() - 1);
        chattingRoomRepository.save(room);

        if(room.getMemberCount()==0){
            Board board = boardRepository.findBoardByChattingRoom(room)
                    .orElseThrow(() -> new ForbiddenException(ErrorCode.BOARD_NOT_FOUND));
            boardRepository.delete(board);
        }
    }

    /**
     * 채팅방 삭제하기
     **/
    @Transactional
    public void deleteRoom(Board board) {
        chattingRoomRepository.deleteByBoard(board);
    }

    /**
     * 기존 메시지 조회 및 반환
     **/
    @Transactional
    public List<Message> getMessagesByRoomId(UUID roomId) {
        ChattingRoom room = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
        return messageRepository.findByChattingRoomOrderByCreatedAtAsc(room);
    }

    /**
     * 채팅방에 처음 입장한 유저인지 확인
     **/
    @Transactional
    public boolean isNewUserInRoom(String name, UUID roomId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return !userRoomRepository.existsByUserAndChattingRoom(user, chattingRoom);
    }
}
