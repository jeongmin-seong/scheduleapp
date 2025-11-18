package com.scheduleapp.repository;

import com.scheduleapp.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 모든 일정을 수정일 기준 내림차순으로 조회
    List<Schedule> findAllByOrderByUpdatedAtDesc();

    // 모든 일정을 수정일 기준 내림차순으로 페이징 조회 (페이징 적용)
    Page<Schedule> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    // 특정 사용자의 일정을 수정일 기준 내림차순으로 조회
    List<Schedule> findByUser_IdOrderByUpdatedAtDesc(Long userId);

    // 모든 일정을 페이징 처리하여 조회
    @Query("SELECT s FROM Schedule s JOIN FETCH s.user ORDER BY s.updatedAt DESC")
    Page<Schedule> findAllWithUser(Pageable pageable);

}
