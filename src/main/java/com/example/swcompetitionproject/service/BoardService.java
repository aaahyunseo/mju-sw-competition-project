package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.board.CreateBoardDto;
import com.example.swcompetitionproject.dto.request.board.UpdateBoardDto;
import com.example.swcompetitionproject.dto.response.board.BoardData;
import com.example.swcompetitionproject.dto.response.board.BoardListData;
import com.example.swcompetitionproject.entity.*;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.ForbiddenException;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.BoardRepository;
import com.example.swcompetitionproject.repository.ChattingRoomRepository;
import com.example.swcompetitionproject.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ChattingService chattingService;
    private final InterestRepository interestRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    /**
     * 게시글 전체 조회
     **/
    @Transactional
    public BoardListData getBoardList(User user, String dormitory) {
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        //해당 건물에 대한 모든 게시물
        List<Board> boards = boardRepository.findAllByDormitoryOrderByCreatedAtDesc(dormitoryType);

        // 가득 찬 게시물과 그렇지 않은 게시물을 저장할 리스트
        List<Board> fullBoards = new LinkedList<>();
        List<Board> notFullBoards = new LinkedList<>();

        // 반복문을 사용해 게시물을 분류
        for (Board board : boards) {
            if (board.getTotal() == chattingRoomRepository.findByBoard(board).get().getMemberCount()) {
                fullBoards.add(board); // 가득 찬 게시물
            } else {
                notFullBoards.add(board); // 인원 미달 게시물
            }
        }

        // 가득 찬 게시물 리스트와 인원 미달 게시물 리스트를 합침
        List<Board> resultBoards = new LinkedList<>();
        resultBoards.addAll(notFullBoards);
        resultBoards.addAll(fullBoards);

        return BoardListData.of(resultBoards, user, interestRepository);
    }

    /**
     * 게시글 상세 조회
     **/
    @Transactional
    public BoardData getBoardById(User user, String dormitory, UUID boardId) {
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        Board board = boardRepository.findBoardByDormitoryAndId(dormitoryType, boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        boolean isLike = interestRepository.existsByUserAndBoard(user, board);
        return BoardData.of(isLike, board);

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
     * 게시글 좋아요 등록
     **/
    @Transactional
    public void likeBoard(User user, UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        //이미 좋아요를 누른 게시물
        if (interestRepository.existsByUserAndBoard(user, board)) {
            throw new UnauthorizedException(ErrorCode.ALREADY_LIKED);
        }

        Interest like = Interest.builder()
                .user(user)
                .board(board)
                .build();
        interestRepository.save(like);
    }

    /**
     * 게시글 좋아요 삭제
     **/
    @Transactional
    public void unlikeBoard(User user, UUID boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        Interest like = interestRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LIKE_NOT_FOUND));

        interestRepository.delete(like);
    }

    /**
     * 관심 게시글 조회
     **/
    @Transactional
    public BoardListData getLikedBoards(User user) {
        List<Board> likedBoards = interestRepository.findBoardsByUser(user);
        return BoardListData.from(likedBoards);
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

