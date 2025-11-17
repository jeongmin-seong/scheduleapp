package com.scheduleapp.repository;

import com.scheduleapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 일정의 모든 댓글 조회 (최신순)
    List<Comment> findByScheduleIdOrderByCreatedAtDesc(Long scheduleId);

    // 특정 일정의 모든 댓글 조회 (User 정보 함께 조회)
    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.schedule.id = :scheduleId ORDER BY c.createdAt DESC")
    List<Comment> findByScheduleIdWithUser(@Param("scheduleId") Long scheduleId);

    // 특정 유저의 모든 댓글 조회 (최신순)
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 특정 일정의 댓글 개수 조회
    long countByScheduleId(Long scheduleId);
}
