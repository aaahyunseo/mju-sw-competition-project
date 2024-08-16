package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.BoardCategory;
import com.example.swcompetitionproject.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
public class BoardDetailResponseDto {
    private UUID id;
    private String title;
    private String content;
    private List<String> categoryList;
    private int current;
    private int total;
    private String createdAt;

    public static BoardDetailResponseDto from(Board board){
        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryList(board.getBoardCategories().stream()
                        .map(boardCategory -> boardCategory.getCategory().getCategory())
                        .collect(Collectors.toList()))
                .current(board.getChattingRoom().getMemberCount())
                .total(board.getTotal())
                .createdAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}

