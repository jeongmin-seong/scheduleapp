package com.scheduleapp.exception;

// 인증 실패 시 발생하는 예외
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
