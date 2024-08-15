package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BoardListData {
    private List<BoardResponseDto> boardDtoList;

    public static BoardListData from(List<Board> boards) {
        return BoardListData.builder()
                .boardDtoList(
                        boards.stream()
                                .map(BoardResponseDto::from)
                                .collect(Collectors.toList()))
                .build();
    }
}

