package com.example.swcompetitionproject.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupDto {
    @NotBlank(message = "이름을 입력해주세요.")
    private String major;

    @NotBlank(message = "구분을 입력해주세요.")
    private String division;

    @NotNull(message = "학년을 입력해주세요.")
    private int grade;

    @NotBlank(message = "학번을 입력해주세요.")
    private String studentNumber;

    @NotBlank(message = "영문과 숫자,특수기호를 조합하여 8~20글자 미만으로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$", message = "비밀번호는 영문과 숫자,특수기호를 조합하여 8~20글자 미만으로 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자에서 최대 20자까지 입력 가능합니다.")
    private String password;

    @NotBlank(message = "사용하실 이름을 입력해주세요.")
    @Size(min = 1, max = 10, message = "이름은 최소 1자에서 최대 10자까지 입력 가능합니다.")
    private String name;
}

