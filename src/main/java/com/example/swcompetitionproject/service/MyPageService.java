package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.user.CreateUserCategoryDto;
import com.example.swcompetitionproject.dto.request.user.ModifyUserNameDto;
import com.example.swcompetitionproject.dto.response.board.MyBoardListData;
import com.example.swcompetitionproject.dto.response.category.CategoryListData;
import com.example.swcompetitionproject.dto.response.user.UserResponseDto;
import com.example.swcompetitionproject.entity.Board;
import com.example.swcompetitionproject.entity.Category;
import com.example.swcompetitionproject.entity.User;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.repository.BoardRepository;
import com.example.swcompetitionproject.repository.CategoryRepository;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;

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
    public MyBoardListData getMyBoard(User user) {
        //내가 작성한 모든 게시판 찾기
        List<Board> myBoards = boardRepository.findAllByUser(user);

        //데이터 리스트로 만들기
        MyBoardListData myBoardListData = MyBoardListData.from(myBoards);

        return myBoardListData;
    }

    /**
     * 유저 카테고리 추가
     */
    public void creatUserCatedory(User user, CreateUserCategoryDto createUserCategoryDto) {

        Category newCategory = Category.builder()
                .category(createUserCategoryDto.getCategory())
                .user(user)
                .build();

        categoryRepository.save(newCategory);
    }

    /**
     * 유저 카테고리 삭제
     */
    public void deleteUserCategory(User user, UUID categoryId) {
        Category category = categoryRepository.findByUserAndId(user, categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }

    /**
     * 유저 카테고리 전체 조회
     */
    public CategoryListData getUserCategoryList(User user) {
        List<Category> categories = categoryRepository.findAllByUser(user);
        return CategoryListData.from(categories);
    }
}
