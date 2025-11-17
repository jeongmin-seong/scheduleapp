package com.scheduleapp.service;

import com.scheduleapp.dto.request.CommentCreateRequest;
import com.scheduleapp.dto.request.CommentUpdateRequest;
import com.scheduleapp.dto.response.CommentResponse;
import com.scheduleapp.entity.Comment;
import com.scheduleapp.entity.Schedule;
import com.scheduleapp.entity.User;
import com.scheduleapp.exception.BusinessException;
import com.scheduleapp.exception.ErrorCode;
import com.scheduleapp.repository.CommentRepository;
import com.scheduleapp.repository.ScheduleRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(CommentCreateRequest request) {
        log.info("Creating comment for schedule id: {}, user id: {}", request.getScheduleId(), request.getUserId());

        // 유저 존재 확인
        User user = userService.findUserById(request.getUserId());

        // 일정 존재 확인
        Schedule schedule = scheduleService.findScheduleById(request.getScheduleId());

        // DTO를 Entity로 변환하여 저장
        Comment comment = request.toEntity(user, schedule);
        Comment savedComment = commentRepository.save(comment);

        log.info("Comment created successfully with id: {}", savedComment.getId());
        return CommentResponse.from(savedComment);
    }

    // 댓글 단건 조회
    public CommentResponse getComment(Long id) {
        log.info("Fetching comment with id: {}", id);

        Comment comment = findCommentById(id);
        return CommentResponse.from(comment);
    }

    // 특정 일정의 전체 댓글 조회 (최신순)
    public List<CommentResponse> getCommentsByScheduleId(Long scheduleId) {
        log.info("Fetching comments for schedule id: {}", scheduleId);

        // 일정 존재 확인
        scheduleService.findScheduleById(scheduleId);

        // N+1 문제 해결: User 정보를 함께 조회
        List<Comment> comments = commentRepository.findBySchedule_IdWithUser(scheduleId);

        log.info("Found {} comments for schedule id: {}", comments.size(), scheduleId);
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // 특정 유저의 전체 댓글 조회 (최신순)
    public List<CommentResponse> getCommentsByUserId(Long userId) {
        log.info("Fetching comments for user id: {}", userId);

        // 유저 존재 확인
        userService.findUserById(userId);

        List<Comment> comments = commentRepository.findByUser_IdOrderByCreatedAtDesc(userId);

        log.info("Found {} comments for user id: {}", comments.size(), userId);
        return comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long id, CommentUpdateRequest request) {
        log.info("Updating comment with id: {}", id);

        // 댓글 조회
        Comment comment = findCommentById(id);

        // 댓글 수정 (변경 감지를 통한 업데이트)
        comment.update(request.getContent());

        log.info("Comment updated successfully with id: {}", id);
        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long id) {
        log.info("Deleting comment with id: {}", id);

        // 댓글 존재 여부 확인
        Comment comment = findCommentById(id);

        // 댓글 삭제
        commentRepository.delete(comment);

        log.info("Comment deleted successfully with id: {}", id);
    }

    // ID로 댓글을 조회하는 public 메서드
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment not found with id: {}", id);
                    return new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
                });
    }
}
