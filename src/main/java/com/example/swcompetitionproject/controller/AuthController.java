package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticationExtractor;
import com.example.swcompetitionproject.authentication.JwtEncoder;
import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import com.example.swcompetitionproject.dto.response.LoginResponseDto;
import com.example.swcompetitionproject.dto.response.ResponseDto;
import com.example.swcompetitionproject.dto.response.TokenResponseDto;
import com.example.swcompetitionproject.service.LoginService;
import com.example.swcompetitionproject.service.TestLoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final TestLoginService testLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto<Void>> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        try {
            TokenResponseDto tokenResponseDto = loginService.login(loginDto);
            String bearerToken = JwtEncoder.encode(tokenResponseDto.getAccessToken());

            ResponseCookie cookie = ResponseCookie.from(AuthenticationExtractor.TOKEN_COOKIE_NAME, bearerToken)
                    .maxAge(Duration.ofMillis(1800000 * 2))
                    .path("/")
                    .httpOnly(true)
                    .sameSite("None").secure(true)
                    .build();
            response.addHeader("set-cookie", cookie.toString());

            return new ResponseEntity<>(LoginResponseDto.loginres(HttpStatus.CREATED, "학교 계정으로 로그인 완료", bearerToken), HttpStatus.CREATED);
        } catch (Exception e) {

            return new ResponseEntity<>(LoginResponseDto.loginres(HttpStatus.UNAUTHORIZED, "학교 계정으로 로그인 실패", null), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/testlogin")
    public  ResponseEntity<ResponseDto<Void>> testlogin(@RequestBody @Valid LoginDto loginDto,HttpServletResponse response){
        try {
            String loginResult = testLoginService.login(loginDto);
            response.addHeader("set-cookie",loginResult);

            return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "학교 계정으로 로그인 완료"), HttpStatus.OK);
        } catch (Exception e) {
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "학교 계정으로 로그인 실패"), HttpStatus.UNAUTHORIZED);

        }
    }

    @GetMapping("/tsetsubject")
    public ResponseEntity<ResponseDto<Void>> tsetsubject(){

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "로그 아웃 완료"), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(final HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(AuthenticationExtractor.TOKEN_COOKIE_NAME, null)
                .maxAge(Duration.ofMillis(0))
                .path("/")
                .httpOnly(true)
                .build();
        response.addHeader("set-cookie", cookie.toString());

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "로그 아웃 완료"), HttpStatus.OK);
    }
}
