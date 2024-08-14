package com.example.swcompetitionproject.controller;

import com.example.swcompetitionproject.authentication.AuthenticationExtractor;
import com.example.swcompetitionproject.dto.response.request.LoginDto;
import com.example.swcompetitionproject.service.login.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        try {
            loginService.login(loginDto);
            log.info("User logged in successfully: {}", loginDto.getId());

            //Set cookies here (example)
            response.addCookie(createCookie("login", "example-session-id"));
            log.info("쿠키 등록 완료");

            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginDto.getId(), e);
            //log.info(String.valueOf(response));

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // JavaScript에서 쿠키 접근 금지
        cookie.setPath("/");      // 모든 경로에서 유효
        cookie.setMaxAge(3600);   // 1시간 동안 유효
        return cookie;
    }
}
