package com.scheduleapp.repository;

import com.scheduleapp.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 모든 일정을 수정일 기준 내림차순으로 조회
    List<Schedule> findAllByOrderByUpdatedAtDesc();

    //특정 사용자의 일정을 수정일 기준 내림차순으로 조회
    List<Schedule> findByUsernameOrderByUpdatedAtDesc(String username);
}
