package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.CreateBoardDto;
import com.example.swcompetitionproject.dto.request.UpdateBoardDto;
import com.example.swcompetitionproject.dto.response.BoardData;
import com.example.swcompetitionproject.dto.response.BoardListData;
import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ChattingService chattingService;

    //게시글 전체 조회
    public BoardListData getBoardList(String dormitory){
        List<Board> boards = boardRepository.findAllByDormitory(dormitory);
        return BoardListData.from(boards);
    }

    //게시글 상세 조회
    public BoardData getBoardById(String dormitory, UUID boardId){
        Board board = boardRepository.findBoardByDormitoryAndId(dormitory, boardId)
                .orElseThrow( () -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        return BoardData.from(board);
    }

    //게시글 작성
    public void createBoard(String dormitory, CreateBoardDto createBoardDto, User user){
        Board newBoard = Board.builder()
                .user(user)
                .title(createBoardDto.getTitle())
                .category(createBoardDto.getCategory())
                .content(createBoardDto.getContent())
                .dormitory(dormitory)
                .build();
        //게시글 작성 완료와 동시에 채팅방 생성
        chattingService.createRoom(user, newBoard);
        boardRepository.save(newBoard);
    }

    //게시글 수정
    public void updateBoard(String dormitory, UUID boardId, UpdateBoardDto updateBoardDto, User user){
        Board newBoard = boardRepository.findBoardByDormitoryAndIdAndUser(dormitory, boardId, user)
                .orElseThrow( () -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        newBoard.setCategory(updateBoardDto.getCategory())
                .setContent(updateBoardDto.getContent());
        boardRepository.save(newBoard);

    }

    //게시글 삭제
    public void deleteBoard(String dormitory, UUID boardId, User user){
        Board board = boardRepository.findBoardByDormitoryAndIdAndUser(dormitory, boardId, user)
                .orElseThrow( () -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        boardRepository.delete(board);
    }
}

