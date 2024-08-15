package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Builder
@Getter
public class BoardData {
    private UUID id;
    private String title;
    private String content;
    private int total;
    private String createdAt;

    public static BoardData from(Board board){
        return BoardData.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .total(board.getTotal())
                .createdAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}

