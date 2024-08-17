package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.board.CreateBoardDto;
import com.example.swcompetitionproject.dto.request.board.UpdateBoardDto;
import com.example.swcompetitionproject.dto.response.board.BoardData;
import com.example.swcompetitionproject.dto.response.board.BoardListData;
import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.BoardCategory;
import com.example.swcompetitionproject.entity.DormitoryType;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.ForbiddenException;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ChattingService chattingService;

    /**
     * 게시글 전체 조회
     **/
    @Transactional
    public BoardListData getBoardList(String dormitory) {
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        List<Board> boards = boardRepository.findAllByDormitoryOrderByCreatedAtDesc(dormitoryType);
        return BoardListData.from(boards);
    }

    /**
     * 게시글 상세 조회
     **/
    @Transactional
    public BoardData getBoardById(String dormitory, UUID boardId) {
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board board = boardRepository.findBoardByDormitoryAndId(dormitoryType, boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        return BoardData.from(board);
    }

    /**
     * 게시글 작성
     **/
    @Transactional
    public void createBoard(String dormitory, CreateBoardDto createBoardDto, User user) {
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
                if (createBoardDto.getTotal() == 0) throw new UnauthorizedException(ErrorCode.NOT_BLANK);
                break;
        }

        Board newBoard = Board.builder()
                .user(user)
                .title(createBoardDto.getTitle())
                .content(createBoardDto.getContent())
                .dormitory(dormitoryType)
                .total(createBoardDto.getTotal())
                .build();

        //유저의 카테고리를 불러와 BoardCategory 생성
        List<BoardCategory> boardCategories = user.getCategory().stream()
                .map(category -> BoardCategory.builder()
                        .board(newBoard)
                        .category(category)
                        .build())
                .collect(Collectors.toList());

        //생성된 BoardCategory 리스트를 Board에 저장
        newBoard.setBoardCategories(boardCategories);

        //게시글 작성 완료와 동시에 채팅방 생성
        boardRepository.save(newBoard);
        chattingService.createRoom(user, newBoard);
    }

    /**
     * 게시글 수정
     **/
    @Transactional
    public void updateBoard(String dormitory, UUID boardId, UpdateBoardDto updateBoardDto, User user) {
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board updateBoard = boardValidate(dormitoryType, boardId, user);
        updateBoard.setContent(updateBoardDto.getContent());
        boardRepository.save(updateBoard);
    }

    /**
     * 게시글 삭제
     **/
    @Transactional
    public void deleteBoard(String dormitory, UUID boardId, User user) {
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board board = boardValidate(dormitoryType, boardId, user);
        boardRepository.delete(board);
        chattingService.deleteRoom(board);
    }

    /**
     * 접근 유저의 게시글 권한 확인
     **/
    public Board boardValidate(DormitoryType dormitoryType, UUID boardId, User user) {
        return boardRepository.findBoardByDormitoryAndIdAndUser(dormitoryType, boardId, user)
                .orElseThrow(() -> new ForbiddenException(ErrorCode.NO_ACCESS));
    }

    /**
     * 기숙사 이름 검증 로직
     **/
    public DormitoryType dormitoryNameValidate(String dormitory) {
        for (DormitoryType type : DormitoryType.values()) {
            if (type.name().equals(dormitory.toUpperCase())) {
                return type;
            }
        }
        throw new UnauthorizedException(ErrorCode.INVALID_DORMITORY);
    }
}

