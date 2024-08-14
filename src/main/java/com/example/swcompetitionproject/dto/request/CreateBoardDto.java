package com.example.swcompetitionproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateBoardDto {
    private String title;
    private String content;
    private List<String> category;
}

