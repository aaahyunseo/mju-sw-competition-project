package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.authentication.PasswordHashEncryption;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ConflictException;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHashEncryption passwordHashEncryption;

    public void validateIsPasswordMatches(String requestedPassword, String userPassword) {
        if (!passwordHashEncryption.matches(requestedPassword, userPassword)) {
            throw new UnauthorizedException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
    }

    public void validateIsDuplicatedStudentNumber(String StudentNumber) {
        if (userRepository.existsByStudentNumber(StudentNumber)) {
            throw new ConflictException(ErrorCode.DUPLICATED_STD_NUM);
        }
    }

    public User findExistingUserByStudentNumber(String StudentNumber) {
        return userRepository.findByStudentNumber(StudentNumber).orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));
    }
}
