package com.scheduleapp.service;

import com.scheduleapp.dto.request.UserCreateRequest;
import com.scheduleapp.dto.request.UserUpdateRequest;
import com.scheduleapp.dto.response.UserResponse;
import com.scheduleapp.entity.User;
import com.scheduleapp.exception.BusinessException;
import com.scheduleapp.exception.ErrorCode;
import com.scheduleapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    // 유저 생성
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating user with email: {}", request.getEmail());

        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // DTO를 Entity로 변환
        User user = request.toEntity();
        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getId());
        return UserResponse.from(savedUser);
    }

    // 유저 단건 조회
    public UserResponse getUser(Long id) {
        log.info("Fetching user with id: {}", id);

        User user = findUserById(id);
        return UserResponse.from(user);
    }

    // 유저 전체 조회
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    // 유저 수정
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user with id: {}", id);

        // 유저 조회
        User user = findUserById(id);

        // 이메일 변경 시 중복 확인
        if (request.getEmail() != null &&
                !user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 유저 정보 수정 (변경 감지를 통한 업데이트)
        user.update(request.getUsername(), request.getEmail());
        log.info("User updated successfully with id: {}", id);
        return UserResponse.from(user);
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        // 유저 존재 여부 확인
        User user = findUserById(id);

        // 유저 삭제
        userRepository.delete(user);
        log.info("User deleted successfully with id: {}", id);
    }

    // ID로 유저를 조회하는 public 메서드 - 유저가 존재하지 않을 경우 예외 발생
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new BusinessException(ErrorCode.USER_NOT_FOUND);
                });
    }
}
