package com.example.swcompetitionproject.dto.response.mainpage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainPageResponseData {
    private String dormitory;
    private int count;
}
