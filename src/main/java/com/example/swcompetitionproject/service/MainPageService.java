package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.response.mainpage.MainPageResponseData;
import com.example.swcompetitionproject.entity.DormitoryType;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MainPageService {
    private final BoardRepository boardRepository;

    /**
     * 기숙사 별 게시글 수 조회하기
     * **/
    public MainPageResponseData getBoardCount(String dormitory){
        DormitoryType dormitoryType = dormitoryNameValidate(dormitory);
        int count = boardRepository.countByDormitory(dormitoryType);

        return MainPageResponseData.builder()
                .dormitory(dormitory)
                .count(count)
                .build();
    }

    /**
     * 기숙사 이름 검증 로직
     * **/
    public DormitoryType dormitoryNameValidate(String dormitory){
        for (DormitoryType type : DormitoryType.values()){
            if (type.name().equals(dormitory.toUpperCase())){
                return type;
            }
        } throw new UnauthorizedException(ErrorCode.INVALID_DORMITORY);
    }
}
