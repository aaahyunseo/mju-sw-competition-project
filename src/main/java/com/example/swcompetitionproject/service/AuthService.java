package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.authentication.JwtTokenProvider;
import com.example.swcompetitionproject.authentication.PasswordHashEncryption;
import com.example.swcompetitionproject.dto.request.auth.LoginDto;
import com.example.swcompetitionproject.dto.request.auth.SignupDto;
import com.example.swcompetitionproject.dto.response.auth.TokenResponseDto;
import com.example.swcompetitionproject.entity.GenderType;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordHashEncryption passwordHashEncryption;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    public TokenResponseDto signup(SignupDto signupDto) {
        userService.validateIsDuplicatedStudentNumber(signupDto.getStudentNumber());

        String plainPassword = signupDto.getPassword();
        String hashedPassword = passwordHashEncryption.encrypt(plainPassword);

        User newUser = User.builder()
                .studentNumber(signupDto.getStudentNumber())
                .password(hashedPassword)
                .name(signupDto.getName())
                .major(signupDto.getMajor())
                .gender(GenderType.valueOf(signupDto.getGender()))
                .build();

        userRepository.save(newUser);

        return createToken(newUser);
    }

    public TokenResponseDto login(LoginDto loginDto) {
        User user = userService.findExistingUserByStudentNumber(loginDto.getStudentNumber());

        userService.validateIsPasswordMatches(loginDto.getPassword(), user.getPassword());

        return createToken(user);
    }

    private TokenResponseDto createToken(User user) {
        String payload = String.valueOf(user.getId());
        String accessToken = jwtTokenProvider.createToken(payload);

        return new TokenResponseDto(accessToken);
    }
}
