package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MyBoardListData {
    private List<MyBoardResponseDto> myBoardDtoList;

    public static MyBoardListData from(List<Board> boards) {
        return MyBoardListData.builder()
                .myBoardDtoList(
                        boards.stream()
                                .map(MyBoardResponseDto::from)
                                .collect(Collectors.toList()))
                .build();
    }
}
