package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.board.CreateBoardDto;
import com.example.swcompetitionproject.dto.request.board.UpdateBoardDto;
import com.example.swcompetitionproject.dto.response.board.BoardData;
import com.example.swcompetitionproject.dto.response.board.BoardListData;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    //게시글 전체 조회
    @GetMapping
    public ResponseEntity<ResponseDto<BoardListData>> getBoardList(@AuthenticatedUser User user, @RequestParam(value = "dormitory") String dormitory) {
        BoardListData boardListData = boardService.getBoardList(dormitory);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 목록 조회 완료", boardListData), HttpStatus.OK);
    }

    //게시글 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto<BoardData>> getBoardById(@AuthenticatedUser User user, @RequestParam(value = "dormitory") String dormitory, @PathVariable UUID boardId) {
        BoardData boardDto = boardService.getBoardById(dormitory, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 조회 완료", boardDto), HttpStatus.OK);
    }

    //게시글 작성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createBoard(@RequestParam(value = "dormitory") String dormitory,
                                                         @Valid @RequestBody CreateBoardDto createBoardDto,
                                                         @AuthenticatedUser User user) {
        boardService.createBoard(dormitory, createBoardDto, user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "게시글 작성 완료"), HttpStatus.CREATED);
    }

    //게시글 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Void>> updateBoard(@RequestParam(value = "dormitory") String dormitory,
                                                         @PathVariable UUID boardId,
                                                         @Valid @RequestBody UpdateBoardDto updateBoardDto,
                                                         @AuthenticatedUser User user) {
        boardService.updateBoard(dormitory, boardId, updateBoardDto, user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 수정 완료"), HttpStatus.OK);
    }


    //게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(@RequestParam(value = "dormitory") String dormitory,
                                                         @PathVariable UUID boardId,
                                                         @AuthenticatedUser User user) {
        boardService.deleteBoard(dormitory, boardId, user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 삭제 완료"), HttpStatus.OK);
    }

    // 게시글 좋아요 등록
    @PostMapping("/{boardId}/like")
    public ResponseEntity<ResponseDto<Void>> likeBoard(@AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.likeBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 좋아요 등록 완료"), HttpStatus.OK);
    }

    // 게시글 좋아요 삭제
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<ResponseDto<Void>> unlikeBoard(@AuthenticatedUser User user, @PathVariable UUID boardId) {
        boardService.unlikeBoard(user, boardId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 좋아요 삭제 완료"), HttpStatus.OK);
    }

    // 사용자가 좋아요를 누른 게시글 목록 조회
    @GetMapping("/likes")
    public ResponseEntity<ResponseDto<BoardListData>> getLikedBoards(@AuthenticatedUser User user) {
        BoardListData interestListData = boardService.getLikedBoards(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "좋아요한 게시글 목록 조회 완료", interestListData), HttpStatus.OK);
    }
}

