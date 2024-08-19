package com.example.swcompetitionproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //BadRequestException
    BAD_REQUEST_TOTAL("4000","기숙사는 2인실 또는 4인실만 존재합니다."),

    //UnauthorizedException
    INVALID_TOKEN("4010", "유효하지 않은 토큰입니다."),
    INVALID_DORMITORY("4012", "유효하지 않은 기숙사입니다."),
    INVALID_GENDER("4013", "유효하지 않은 성별입니다."),
    ROOM_FULL("4014", "채팅방에 참여 가능한 인원이 가득찼습니다."),
    ALREADY_LIKED("4015", "이미 관심글로 등록되었습니다."),
    USER_ALREADY_IN_ROOM("4016", "이미 참여중인 채팅방입니다."),

    //ForbiddenException
    NO_ACCESS("4030", "접근 권한이 없습니다."),
    FORBIDDEN_GENDER_ROOM("4031", "다른 성별의 기숙사 채팅방에는 참여할 수 없습니다."),
    FORBIDDEN_GENDER_BOARD("4032","다른 성별의 기숙사 게시판에는 게시글을 작성할 수 없습니다."),

    //NotFoundException
    COOKIE_NOT_FOUND("4040", "쿠키를 찾을 수 없습니다."),
    USER_NOT_FOUND("4041", "유저를 찾을 수 없습니다."),
    ROOM_NOT_FOUND("4042", "방을 찾을 수 없습니다."),
    BOARD_NOT_FOUND("4043", "게시글을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND("4044", "카테고리를 찾을 수 없습니다"),
    LIKE_NOT_FOUND("4045", "관심글을 찾을 수 없습니다."),

    //ConflictException
    DUPLICATED_NAME("4090", "이미 사용중인 이름입니다."),

    //ValidationException
    NOT_NULL("9001", "필수값이 누락되었습니다."),
    NOT_BLANK("9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("9003", "이메일 형식에 맞지 않습니다."),
    LENGTH("9004", "길이가 유효하지 않습니다.");

    private final String code;
    private final String message;

    //Dto의 어노테이션을 통해 발생한 에러코드를 반환
    public static ErrorCode resolveValidationErrorCode(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL;
            case "NotBlank" -> NOT_BLANK;
            case "Pattern" -> REGEX;
            case "Length" -> LENGTH;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}
