package com.scheduleapp.exception;

// 잘못된 요청 시 발생하는 예외
public class InvalidRequestException extends BusinessException {
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
