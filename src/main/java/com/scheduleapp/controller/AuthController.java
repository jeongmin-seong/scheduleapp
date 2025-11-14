package com.scheduleapp.controller;

import com.scheduleapp.common.SessionConst;
import com.scheduleapp.dto.request.LoginRequest;
import com.scheduleapp.dto.request.SignupRequest;
import com.scheduleapp.dto.response.LoginResponse;
import com.scheduleapp.dto.response.UserResponse;
import com.scheduleapp.entity.User;
import com.scheduleapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        log.info("POST /api/auth/signup - Signup request");

        UserResponse response = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        log.info("POST /api/auth/login - Login request for email: {}", request.getEmail());

        // 1. 이메일과 비밀번호 검증
        User user = authService.login(request);

        // 2. 세션 생성 (true: 세션이 없으면 새로 생성)
        HttpSession session = httpRequest.getSession(true);

        // 3. 세션에 로그인 유저 ID 저장
        session.setAttribute(SessionConst.LOGIN_USER_ID, user.getId());

        log.info("Login successful - User ID: {}, Session ID: {}",
                user.getId(), session.getId());

        // 4. 로그인 성공 응답 반환
        LoginResponse response = LoginResponse.success(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        return ResponseEntity.ok(response);
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
        log.info("POST /api/auth/logout - Logout request");

        // 1. 기존 세션 조회 (false: 세션이 없으면 null 반환)
        HttpSession session = httpRequest.getSession(false);

        // 2. 세션이 존재하면 무효화
        if (session != null) {
            Long userId = (Long) session.getAttribute(SessionConst.LOGIN_USER_ID);
            String sessionId = session.getId();

            // 세션 무효화 (세션 데이터 모두 삭제)
            session.invalidate();

            log.info("Logout successful - User ID: {}, Session ID: {} invalidated",
                    userId, sessionId);
        } else {
            log.info("Logout request but no active session found");
        }

        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    // 현재 로그인 사용자 정보 조회 API
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(HttpServletRequest httpRequest) {
        log.info("GET /api/auth/me - Current user info request");

        // 세션에서 유저 ID 조회
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            log.warn("No active session found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = (Long) session.getAttribute(SessionConst.LOGIN_USER_ID);
        if (userId == null) {
            log.warn("No user ID in session");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 유저 정보 조회
        UserResponse response = authService.getUserById(userId);

        log.info("Current user info retrieved: {}", userId);
        return ResponseEntity.ok(response);
    }
}
