package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class BoardResponseDto {
    private UUID id;
    private String title;

    public static BoardResponseDto from(Board board){
        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .build();
    }
}

