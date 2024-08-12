package com.example.swcompetitionproject.exception;

public class ConflictException extends CustomException {
    public ConflictException(ErrorCode errorCode){
        super(errorCode);
    }
}
