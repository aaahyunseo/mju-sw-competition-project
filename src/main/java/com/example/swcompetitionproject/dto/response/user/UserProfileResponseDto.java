package com.example.swcompetitionproject.dto.response.user;

import com.example.swcompetitionproject.dto.response.category.CategoryListData;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponseDto {
    private String name;
    private CategoryListData categoryListData;
}
