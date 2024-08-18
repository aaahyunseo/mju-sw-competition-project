package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.GenderType;
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
    private GenderType gender;
    private boolean like;
    private String createdAt;

    public static BoardDetailResponseDto of(Boolean isLike, Board board){
        return BoardDetailResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .categoryList(board.getBoardCategories().stream()
                        .map(boardCategory -> boardCategory.getCategory().getCategory())
                        .collect(Collectors.toList()))
                .current(board.getChattingRoom().getMemberCount())
                .total(board.getTotal())
                .gender(board.getUser().getGender())
                .like(isLike)
                .createdAt(board.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}

