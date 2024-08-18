package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.dto.response.auth.ResponseDto;
import com.example.swcompetitionproject.dto.response.mainpage.MainPageResponseData;
import com.example.swcompetitionproject.service.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainPageController {
    public final MainPageService mainPageService;

    @GetMapping
    public ResponseEntity<ResponseDto<MainPageResponseData>> getBoardList(@RequestParam(value = "dormitory")String dormitory) {
        MainPageResponseData mainPageResponseData = mainPageService.getBoardCount(dormitory);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "게시글 갯수 조회 완료", mainPageResponseData), HttpStatus.OK);
    }
}
