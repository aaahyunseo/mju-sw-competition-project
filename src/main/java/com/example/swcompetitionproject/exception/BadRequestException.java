package com.example.swcompetitionproject.exception;

public class BadRequestException extends CustomException{
    public BadRequestException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
