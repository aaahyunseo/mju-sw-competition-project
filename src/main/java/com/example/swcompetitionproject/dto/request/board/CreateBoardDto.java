package com.example.swcompetitionproject.dto.request.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateBoardDto {
    @NotBlank(message = "게시글 제목을 입력하세요.")
    @Size(max = 20)
    private String title;
    @NotBlank(message = "게시글 내용을 입력하세요.")
    @Size(max = 500)
    private String content;
    private int total;
}

