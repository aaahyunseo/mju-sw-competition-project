package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.board.CreateBoardDto;
import com.example.swcompetitionproject.dto.request.board.UpdateBoardDto;
import com.example.swcompetitionproject.dto.response.board.BoardData;
import com.example.swcompetitionproject.dto.response.board.BoardListData;
import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.DormitoryType;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
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

    /**
     * 게시글 전체 조회
     * **/
    public BoardListData getBoardList(String dormitory){
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        List<Board> boards = boardRepository.findAllByDormitoryOrderByCreatedAtDesc(dormitoryType);
        return BoardListData.from(boards);
    }

    /**
     * 게시글 상세 조회
     * **/
    public BoardData getBoardById(String dormitory, UUID boardId){
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board board = boardRepository.findBoardByDormitoryAndId(dormitoryType, boardId)
                .orElseThrow( () -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        return BoardData.from(board);
    }

    /**
     * 게시글 작성
     * **/
    public void createBoard(String dormitory, CreateBoardDto createBoardDto, User user){
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);

        switch (dormitoryType) {
            case DORMITORY4:
                createBoardDto.setTotal(2);
                break;
            case DORMITORY5:
            case MYOUNGDEOK:
                createBoardDto.setTotal(4);
                break;
            default:
                break;
        }

        Board newBoard = Board.builder()
                .user(user)
                .title(createBoardDto.getTitle())
                .content(createBoardDto.getContent())
                .dormitory(dormitoryType)
                .total(createBoardDto.getTotal())
                .build();

        //게시글 작성 완료와 동시에 채팅방 생성
        boardRepository.save(newBoard);
        chattingService.createRoom(user, newBoard);
    }

    /**
     * 게시글 수정
     * **/
    public void updateBoard(String dormitory, UUID boardId, UpdateBoardDto updateBoardDto, User user){
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board updateBoard = boardValidate(dormitoryType, boardId, user);

        updateBoard.setContent(updateBoardDto.getContent());

        switch (dormitoryType) {
            case DORMITORY4:
                updateBoard.setTotal(2);
                break;
            case DORMITORY5:
            case MYOUNGDEOK:
                updateBoard.setTotal(4);
                break;
            default:
                updateBoard.setTotal(updateBoard.getTotal());
                break;
        }

        boardRepository.save(updateBoard);
    }

    /**
     * 게시글 삭제
     * **/
    public void deleteBoard(String dormitory, UUID boardId, User user){
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board board = boardValidate(dormitoryType, boardId, user);
        boardRepository.delete(board);
        chattingService.deleteRoom(board);
    }

    /**
     * 게시글 존재 여부 확인 로직
     * **/
    public Board boardValidate(DormitoryType dormitoryType, UUID boardId, User user){
        return boardRepository.findBoardByDormitoryAndIdAndUser(dormitoryType, boardId, user)
                .orElseThrow( () -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    /**
     * 기숙사 이름 검증 로직
     * **/
    public DormitoryType dormitoryNameValidate(String dormitory){
        for (DormitoryType type : DormitoryType.values()){
            if (type.name().equals(dormitory.toUpperCase())){
                return type;
            }
        } throw new UnauthorizedException(ErrorCode.INVALID_DORMITORY);
    }
}

