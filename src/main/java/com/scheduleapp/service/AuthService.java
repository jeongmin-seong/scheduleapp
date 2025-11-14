package com.scheduleapp.service;

import com.scheduleapp.dto.request.LoginRequest;
import com.scheduleapp.dto.request.SignupRequest;
import com.scheduleapp.dto.response.UserResponse;
import com.scheduleapp.entity.User;
import com.scheduleapp.exception.BusinessException;
import com.scheduleapp.exception.ErrorCode;
import com.scheduleapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    // 회원가입
    @Transactional
    public UserResponse signup(SignupRequest request) {
        log.info("Signup request for email: {}", request.getEmail());

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호는 현재 평문으로 저장 (Lv4에서 암호화 추가 예정)
        // 실제 프로덕션 환경에서는 반드시 암호화해야 함
        String encodedPassword = request.getPassword();

        // DTO를 Entity로 변환하여 저장
        User user = request.toEntity(encodedPassword);
        User savedUser = userRepository.save(user);

        log.info("User signed up successfully with id: {}", savedUser.getId());
        return UserResponse.from(savedUser);
    }

    // 로그인
    public User login(LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());

        // 이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Invalid credentials - user not found: {}", request.getEmail());
                    return new BusinessException(ErrorCode.INVALID_CREDENTIALS);
                });

        // 비밀번호 검증 (현재는 평문 비교, 실제로는 암호화된 비밀번호 비교해야 함)
        if (!user.getPassword().equals(request.getPassword())) {
            log.error("Invalid credentials - password mismatch for email: {}", request.getEmail());
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        log.info("User logged in successfully: {}", user.getId());
        return user;
    }

    // 유저 ID로 유저 정보 조회
    public UserResponse getUserById(Long userId) {
        log.info("Fetching user by id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new BusinessException(ErrorCode.USER_NOT_FOUND);
                });

        return UserResponse.from(user);
    }
}
