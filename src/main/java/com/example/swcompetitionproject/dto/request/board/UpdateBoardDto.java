package com.example.swcompetitionproject.dto.request.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoardDto {
    @NotBlank(message = "게시글 내용을 입력하세요.")
    @Size(max = 500)
    private String content;
    private int total;
}
