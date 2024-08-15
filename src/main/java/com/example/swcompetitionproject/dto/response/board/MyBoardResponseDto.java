package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class MyBoardResponseDto {
    private UUID id;
    private String title;
    private LocalDateTime createdAt;

    public static MyBoardResponseDto from(Board board){
        return MyBoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
