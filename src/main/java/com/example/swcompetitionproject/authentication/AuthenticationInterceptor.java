package com.example.swcompetitionproject.authentication;

import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {
        String accessToken = AuthenticationExtractor.extract(request);
        UUID userId = UUID.fromString(jwtTokenProvider.getPayload(accessToken));
        User user = findExistingUser(userId);
        authenticationContext.setPrincipal(user);
        return true;
    }

    private User findExistingUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
