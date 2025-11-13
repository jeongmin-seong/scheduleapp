package com.scheduleapp.controller;

import com.scheduleapp.dto.request.UserCreateRequest;
import com.scheduleapp.dto.request.UserUpdateRequest;
import com.scheduleapp.dto.response.UserResponse;
import com.scheduleapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 유저 생성 API
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        log.info("POST /api/users - Creating user");

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 유저 단건 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("GET /api/users/{} - Fetching user", id);

        UserResponse response = userService.getUser(id);

        return ResponseEntity.ok(response);
    }

    // 유저 전체 조회 API
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET /api/users - Fetching all users");

        List<UserResponse> responses = userService.getAllUsers();

        return ResponseEntity.ok(responses);
    }

    // 유저 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        log.info("PUT /api/users/{} - Updating user", id);

        UserResponse response = userService.updateUser(id, request);

        return ResponseEntity.ok(response);
    }

    // 유저 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Deleting user", id);

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
