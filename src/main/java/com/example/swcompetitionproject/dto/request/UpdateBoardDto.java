package com.example.swcompetitionproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateBoardDto {
    private String content;
    private List<String> category;
}
