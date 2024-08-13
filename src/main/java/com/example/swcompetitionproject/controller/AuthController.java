package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticationExtractor;
import com.example.swcompetitionproject.authentication.JwtEncoder;
import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import com.example.swcompetitionproject.dto.request.auth.SignupDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.dto.response.auth.TokenResponseDto;
import com.example.swcompetitionproject.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<Void>> signup(@RequestBody @Valid SignupDto signupDto) {
        authService.signup(signupDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "회원가입 완료"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = authService.login(loginDto);
        String token = JwtEncoder.encode(tokenResponseDto.getAccessToken());

        ResponseCookie cookie = ResponseCookie.from(AuthenticationExtractor.TOKEN_COOKIE_NAME, token)
                .maxAge(Duration.ofMillis(1800000))
                .path("/")
                .httpOnly(true)
                .sameSite("None").secure(true)
                .build();
        response.addHeader("set-cookie", cookie.toString());

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "로그인 완료"), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(final HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("AccessToken", null)
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader("set-cookie", cookie.toString());

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "로그아웃 완료"), HttpStatus.OK);
    }
}
