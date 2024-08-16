package com.example.swcompetitionproject.dto.response.category;

import com.example.swcompetitionproject.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CategoryResponseDto {
    private UUID id;
    private String category;

    public static CategoryResponseDto from(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .category(category.getCategory())
                .build();
    }
}
