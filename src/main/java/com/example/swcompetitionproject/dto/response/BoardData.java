package com.example.swcompetitionproject.dto.response;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class BoardData {
    private UUID id;
    private String title;
    private List<String> category;
    private String content;

    public static BoardData from(Board board){
        return BoardData.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .content(board.getContent())
                .build();
    }
}

