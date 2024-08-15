package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticatedUser;
import com.example.swcompetitionproject.dto.request.user.ModifyUserNameDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.service.MyPageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class MypageController {
    private final MyPageService myPageService;

    /**
     * 유저 이름 변경
     */
    @PatchMapping("info/name")
    public ResponseEntity<ResponseDto<Void>> modifyUserName(@AuthenticatedUser User user, @Valid @RequestBody ModifyUserNameDto modifyUserNameDto){
        myPageService.modifyUserName(user,modifyUserNameDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "이름 수정 완료"), HttpStatus.OK);
    }
}
