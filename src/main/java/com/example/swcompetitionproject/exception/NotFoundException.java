package com.example.swcompetitionproject.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
