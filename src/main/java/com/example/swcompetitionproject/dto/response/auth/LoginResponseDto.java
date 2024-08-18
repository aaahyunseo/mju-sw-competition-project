package com.example.swcompetitionproject.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class LoginResponseDto<T> {
    private final String statusCode;
    private final String message;
    private final String accessToken;

    //로그인시 사용하는 응답
    // 반환 데이터가 있는 API를 위한 응답
    public static <T> LoginResponseDto<T> loginres(final HttpStatusCode statusCode, final String message,final String accessToken) {
        return new LoginResponseDto<>(statusCode.toString(), message,accessToken);
    }
}