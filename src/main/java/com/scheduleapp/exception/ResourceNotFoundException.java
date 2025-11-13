package com.scheduleapp.exception;

// 리소스를 찾을 수 없을 때 발생하는 예외
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
