package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.authentication.JwtTokenProvider;
import com.example.swcompetitionproject.authentication.PasswordHashEncryption;
import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import com.example.swcompetitionproject.dto.request.auth.SignupDto;
import com.example.swcompetitionproject.dto.response.auth.TokenResponseDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordHashEncryption passwordHashEncryption;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    public void signup(SignupDto signupDto) {

        // 비밀번호 암호화
        String plainPassword = signupDto.getPassword();
        String hashedPassword = passwordHashEncryption.encrypt(plainPassword);

        User newUser = User.builder()
                .password(hashedPassword)
                .name(signupDto.getName())
                .studentNumber(signupDto.getStudentNumber())
                .division(signupDto.getDivision())
                .major(signupDto.getMajor())
                .grade(signupDto.getGrade())
                .build();
        userRepository.save(newUser);
    }

    /**
     * 로그인
     */
    public TokenResponseDto login(LoginDto loginDto) {
        // 유저 검증
        User user = findExistingUserByStudent_number(loginDto.getStudentNumber());

        // 비밀번호 검증
        if (!passwordHashEncryption.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_FOUND);
        }

        // 토큰 생성
        String payload = String.valueOf(user.getId());
        String accessToken = jwtTokenProvider.createToken(payload);

        return new TokenResponseDto(accessToken);
    }

    private User findExistingUserByStudent_number(String number) {
        return userRepository.findByStudentNumber(number).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}