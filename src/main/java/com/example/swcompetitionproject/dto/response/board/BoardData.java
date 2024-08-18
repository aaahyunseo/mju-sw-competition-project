package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
public class BoardData {
    private UUID id;
    private UUID roomId;
    private String title;
    private String content;
    private List<String> categoryList;
    private boolean like;
    private int current;
    private int total;
    private String createdAt;

    public static BoardData of(Boolean isLike, Board board) {
        return BoardData.builder()
                .id(board.getId())
                .roomId(board.getChattingRoom().getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryList(board.getBoardCategories().stream()
                        .map(boardCategory -> boardCategory.getCategory().getCategory())
                        .collect(Collectors.toList()))
                .like(isLike)
                .current(board.getChattingRoom().getMemberCount())
                .total(board.getTotal())
                .createdAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}

