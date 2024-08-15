package com.example.swcompetitionproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserNameDto {

    @NotBlank(message = "사용 할 이름을 입력해 주세요.")
    private String name;
}
