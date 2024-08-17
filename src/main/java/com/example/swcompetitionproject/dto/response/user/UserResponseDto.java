package com.example.swcompetitionproject.dto.response.user;

import com.example.swcompetitionproject.entity.GenderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    //이름
    private String name;

    //전공(소속)
    private String major;

    //학번
    private String studentNumber;

    //성별
    private GenderType gender;
}
