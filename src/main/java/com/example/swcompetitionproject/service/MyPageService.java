package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.user.ModifyUserNameDto;
import com.example.swcompetitionproject.dto.response.board.MyBoardListData;
import com.example.swcompetitionproject.dto.response.user.UserResponseDto;
import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.repository.BoardRepository;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

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

    /**
     * 나의 게시판 조회
     */
    public MyBoardListData getMyBoard(User user){
        //내가 작성한 모든 게시판 찾기
        List<Board> myBoards=boardRepository.findAllByUser(user);

        //데이터 리스트로 만들기
        MyBoardListData myBoardListData = MyBoardListData.from(myBoards);

        return myBoardListData;
    }

}
