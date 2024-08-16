package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.user.CreateUserCategoryDto;
import com.example.swcompetitionproject.dto.request.user.ModifyUserNameDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.dto.response.board.MyBoardListData;
import com.example.swcompetitionproject.dto.response.category.CategoryListData;
import com.example.swcompetitionproject.dto.response.user.UserResponseDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.MyPageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class MypageController {
    private final MyPageService myPageService;

    /**
     * 유저 이름 변경
     */
    @PatchMapping("info/name")
    public ResponseEntity<ResponseDto<Void>> modifyUserName(@AuthenticatedUser User user, @Valid @RequestBody ModifyUserNameDto modifyUserNameDto) {
        myPageService.modifyUserName(user, modifyUserNameDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "이름 수정 완료"), HttpStatus.OK);
    }

    /**
     * 유저 정보 조회
     */
    @GetMapping("/info")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUserInfo(@AuthenticatedUser User user) {
        UserResponseDto userResponseDto = myPageService.getUserInfo(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 정보 조회 완료", userResponseDto), HttpStatus.OK);
    }

    /**
     * 나의 게시판 조회
     */
    @GetMapping("/board")
    public ResponseEntity<ResponseDto<MyBoardListData>> getMyrBoard(@AuthenticatedUser User user) {
        MyBoardListData myBoards = myPageService.getMyBoard(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "나의 게시판 조회 완료", myBoards), HttpStatus.OK);
    }

    /**
     * 유저 카테고리 추가
     */
    @PostMapping("/category")
    public ResponseEntity<ResponseDto<Void>> creatUserCategory(@AuthenticatedUser User user, @RequestBody CreateUserCategoryDto createUserCategoryDto) {
        myPageService.creatUserCatedory(user, createUserCategoryDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 카테고리 추가 완료", null), HttpStatus.OK);
    }

    /**
     * 유저 카테고리 삭제
     */
    @DeleteMapping("/category/{categoryid}")
    public ResponseEntity<ResponseDto<Void>> deleteUserCategory(@AuthenticatedUser User user, @PathVariable UUID categoryid) {
        myPageService.deleteUserCategory(categoryid);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 카테고리 삭제 완료", null), HttpStatus.OK);
    }

    /**
     * 유저 카테고리 전체 조회
     */
    @GetMapping("/category")
    public ResponseEntity<ResponseDto<CategoryListData>> getUserCategoryList(@AuthenticatedUser User user) {
        CategoryListData categoryList=myPageService.getUserCategoryList(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 카테고리 전체 조회 완료", categoryList), HttpStatus.OK);
    }
}
