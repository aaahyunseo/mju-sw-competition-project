package com.example.swcompetitionproject.dto.response.category;

import com.example.swcompetitionproject.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CategoryListData {
    private List<CategoryResponseDto> categoryResponseDto;

    public static CategoryListData from(List<Category> categories) {
        return CategoryListData.builder()
                .categoryResponseDto(
                        categories.stream()
                                .map(CategoryResponseDto::from)
                                .collect(Collectors.toList()))
                .build();
    }
}
