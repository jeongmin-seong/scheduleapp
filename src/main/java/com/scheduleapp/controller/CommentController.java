package com.scheduleapp.controller;

import com.scheduleapp.dto.request.CommentCreateRequest;
import com.scheduleapp.dto.request.CommentUpdateRequest;
import com.scheduleapp.dto.response.CommentResponse;
import com.scheduleapp.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    // 댓글 생성 API
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentCreateRequest request
    ) {
        log.info("POST /api/comments - Creating comment for schedule: {}", request.getScheduleId());

        CommentResponse response = commentService.createComment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 댓글 단건 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        log.info("GET /api/comments/{} - Fetching comment", id);

        CommentResponse response = commentService.getComment(id);

        return ResponseEntity.ok(response);
    }


    // 특정 일정의 댓글 목록 조회 API
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByScheduleId(
            @RequestParam Long scheduleId
    ) {
        log.info("GET /api/comments?scheduleId={} - Fetching comments", scheduleId);

        List<CommentResponse> responses = commentService.getCommentsByScheduleId(scheduleId);

        return ResponseEntity.ok(responses);
    }

    // 특정 유저의 댓글 목록 조회 API
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUserId(@PathVariable Long userId) {
        log.info("GET /api/comments/user/{} - Fetching user's comments", userId);

        List<CommentResponse> responses = commentService.getCommentsByUserId(userId);

        return ResponseEntity.ok(responses);
    }

    // 댓글 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        log.info("PUT /api/comments/{} - Updating comment", id);

        CommentResponse response = commentService.updateComment(id, request);

        return ResponseEntity.ok(response);
    }

    // 댓글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("DELETE /api/comments/{} - Deleting comment", id);

        commentService.deleteComment(id);

        return ResponseEntity.noContent().build();
    }
}