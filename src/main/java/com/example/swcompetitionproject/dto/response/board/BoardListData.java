package com.example.swcompetitionproject.dto.response.board;

import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.repository.InterestRepository;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class BoardListData {
    private List<BoardDetailResponseDto> boardDtoList;

    public static BoardListData of(List<Board> boards, User user, InterestRepository interestRepository) {
        return BoardListData.builder()
                .boardDtoList(
                        boards.stream()
                                .map(board -> {
                                    boolean isLiked = interestRepository.existsByUserAndBoard(user, board);
                                    return BoardDetailResponseDto.of(isLiked, board);
                                })
                                .collect(Collectors.toList()))
                .build();
    }

    public static BoardListData from(List<Board> boards) {
        return BoardListData.builder()
                .boardDtoList(
                        boards.stream()
                                .map(board -> BoardDetailResponseDto.of(true, board))
                                .collect(Collectors.toList()))
                .build();
    }
}

