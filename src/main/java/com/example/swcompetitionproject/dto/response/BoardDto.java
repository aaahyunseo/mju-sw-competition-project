package com.example.swcompetitionproject.dto.response;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class BoardDto {
    private UUID id;
    private String title;
    private List<String> category;

    public static BoardDto from(Board board){
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .build();
    }
}

