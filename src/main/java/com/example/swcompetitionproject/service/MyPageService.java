package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.user.ModifyUserNameDto;
import com.example.swcompetitionproject.dto.response.user.UserResponseDto;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    /**
     * 유저 이름 변경
     */
    public void modifyUserName(User user, ModifyUserNameDto modifyUserNameDto) {
        //유저의 이름만 변경
        user.setName(modifyUserNameDto.getName());
        userRepository.save(user);
    }

    /**
     * 유저 정보 조회
     */
    public UserResponseDto getUserInfo(User user) {
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .name(user.getName())
                .major(user.getMajor())
                .studentNumber(user.getStudentNumber())
                .build();
        return userResponseDto;
    }

}
