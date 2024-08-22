package com.example.swcompetitionproject.service;

import com.example.swcompetitionproject.dto.request.user.CreateUserCategoryDto;
import com.example.swcompetitionproject.dto.request.user.ModifyUserInfoDto;
import com.example.swcompetitionproject.dto.request.user.UserInfoDto;
import com.example.swcompetitionproject.dto.request.user.UserProfileDto;
import com.example.swcompetitionproject.dto.response.board.MyBoardListData;
import com.example.swcompetitionproject.dto.response.category.CategoryListData;
import com.example.swcompetitionproject.dto.response.user.UserProfileResponseDto;
import com.example.swcompetitionproject.dto.response.user.UserResponseDto;
import com.example.swcompetitionproject.entity.*;
import com.example.swcompetitionproject.exception.ConflictException;
import com.example.swcompetitionproject.exception.ErrorCode;
import com.example.swcompetitionproject.exception.NotFoundException;
import com.example.swcompetitionproject.exception.UnauthorizedException;
import com.example.swcompetitionproject.repository.BoardCategoryRepository;
import com.example.swcompetitionproject.repository.BoardRepository;
import com.example.swcompetitionproject.repository.CategoryRepository;
import com.example.swcompetitionproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    /**
     * 유저 정보 등록
     */
    public void userInfoSave(User user, UserInfoDto userInfoDto) {
        GenderType genderType = genderValidate(userInfoDto.getGender());

        //이름 중복 방지
        if (userRepository.findByName(userInfoDto.getName()).isPresent()) {
            throw new ConflictException(ErrorCode.DUPLICATED_NAME);
        }
        //유저의 이름과 성별 필수
        user.setName(userInfoDto.getName()).setGender(genderType);
        userRepository.save(user);
    }

    /**
     * 유저 정보 변경
     */
    public void modifyUserInfo(User user, ModifyUserInfoDto modifyUserInfoDto) {
        //이름 중복 방지
        if (userRepository.findByName(modifyUserInfoDto.getName()).isPresent()) {
            throw new ConflictException(ErrorCode.DUPLICATED_NAME);
        }
        //유저의 이름만 변경
        user.setName(modifyUserInfoDto.getName());
        userRepository.save(user);
    }

    /**
     * 유저 정보 조회
     */
    public UserResponseDto getUserInfo(User user) {
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .major(user.getMajor())
                .studentNumber(user.getStudentNumber())
                .gender(user.getGender())
                .build();

        return userResponseDto;
    }

    /**
     * 나의 게시판 조회
     */
    public MyBoardListData getMyBoard(User user) {
        //내가 작성한 모든 게시판 찾기
        List<Board> myBoards = boardRepository.findAllByUserOrderByCreatedAtDesc(user);

        //데이터 리스트로 만들기
        MyBoardListData myBoardListData = MyBoardListData.from(myBoards);

        return myBoardListData;
    }

    /**
     * 유저 카테고리 추가
     */
    public void creatUserCategory(User user, CreateUserCategoryDto createUserCategoryDto) {

        Category newCategory = Category.builder()
                .category(createUserCategoryDto.getCategory())
                .user(user)
                .build();
        categoryRepository.save(newCategory);

        // 유저의 모든 게시글에 대해 새로운 BoardCategory 생성
        List<Board> userBoards = boardRepository.findAllByUser(user);
        List<BoardCategory> newBoardCategories = userBoards.stream()
                .map(board -> BoardCategory.builder()
                        .board(board)
                        .category(newCategory)
                        .build())
                .collect(Collectors.toList());

        boardCategoryRepository.saveAll(newBoardCategories);
    }

    /**
     * 유저 카테고리 삭제
     */
    public void deleteUserCategory(User user, UUID categoryId) {
        Category category = categoryRepository.findByUserAndId(user, categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        // 해당 카테고리와 연관된 모든 BoardCategory 삭제
        List<BoardCategory> boardCategories = boardCategoryRepository.findAllByCategory(category);
        boardCategoryRepository.deleteAll(boardCategories);
        categoryRepository.delete(category);
    }

    /**
     * 유저 카테고리 전체 조회
     */
    public CategoryListData getUserCategoryList(User user) {
        List<Category> categories = categoryRepository.findAllByUserOrderByCreatedAtAsc(user);
        return CategoryListData.from(categories);
    }

    /**
     * 성별 타입 검증 로직
     **/
    public GenderType genderValidate(String gender) {
        for (GenderType type : GenderType.values()) {
            if (type.name().equals(gender.toUpperCase())) {
                return type;
            }
        }
        throw new UnauthorizedException(ErrorCode.INVALID_GENDER);
    }

    /**
     * 유저 프로필 조회
     */
    public UserProfileResponseDto getUserProfile(UserProfileDto userProfileDto) {
        User profileUser = userRepository.findByName(userProfileDto.getName())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        List<Category> categoriesLsit = categoryRepository.findAllByUserOrderByCreatedAtAsc(profileUser);
        CategoryListData categories = CategoryListData.from(categoriesLsit);

        UserProfileResponseDto userProfileResponseDto = UserProfileResponseDto.builder()
                .categoryListData(categories)
                .name(profileUser.getName())
                .build();

        return userProfileResponseDto;
    }
}
