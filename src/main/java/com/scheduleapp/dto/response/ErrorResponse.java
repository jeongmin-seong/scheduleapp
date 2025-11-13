package com.scheduleapp.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    // HTTP 상태 코드
    private final int status;

    // 에러 메세지
    private final String message;

    // 에러 발생 시각
    private final LocalDateTime timestamp;

    // 요청 경로
    private final String path;

    // 정적 팩토리 메서드
    public static ErrorResponse of(int status, String message, String path) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
}
