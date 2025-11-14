package com.scheduleapp.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long userId;
    private String username;
    private String email;
    private String message;

    // 로그인 응답 생성
    public static LoginResponse success(Long userId, String username, String email) {
        return LoginResponse.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .message("로그인에 성공했습니다.")
                .build();
    }
}
