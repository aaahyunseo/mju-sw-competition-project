package com.example.swcompetitionproject.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    @NotBlank(message = "조회 할 이름을 입력해 주세요.")
    private String name;
}
