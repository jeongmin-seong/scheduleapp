package com.scheduleapp.service;

import com.scheduleapp.dto.request.ScheduleCreateRequest;
import com.scheduleapp.dto.request.ScheduleUpdateRequest;
import com.scheduleapp.dto.response.ScheduleResponse;
import com.scheduleapp.entity.Schedule;
import com.scheduleapp.exception.BusinessException;
import com.scheduleapp.exception.ErrorCode;
import com.scheduleapp.repository.ScheduleRepository;
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
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 일정 생성
    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        log.info("Creating schedule for user: {}", request.getName());

        // DTO를 Entity로 변환하여 저장
        Schedule schedule = request.toEntity();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        log.info("Schedule created succesfully with id: {}", savedSchedule.getId());
        return ScheduleResponse.from(savedSchedule);
    }

    // 일정 단건 조회
    @Transactional
    public ScheduleResponse getSchedule(Long id) {
        log.info("Fetching schedule for user: {}", id);

        Schedule schedule = findScheduleById(id);
        return ScheduleResponse.from(schedule);
    }

    // 일정 전체 조회(최신순)
    public List<ScheduleResponse> getAllSchedules() {
        log.info("Fetching all schedules");

        List<Schedule> schedules = scheduleRepository.findAllByOrderByUpdatedAtDesc();

        return schedules.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // 일정 수정
    @Transactional
    public ScheduleResponse updateSchedule(Long id, ScheduleUpdateRequest request) {
        log.info("Updating schedule with id: {}", id);

        // 일정 조회
        Schedule schedule = findScheduleById(id);

        // 일정 수정 (변경 감지를 통한 업데이트)
        schedule.update(request.getTitle(), request.getContent());

        log.info("Schedule updated successfully with id: {}", id);
        return ScheduleResponse.from(schedule);
    }

    // 일정 삭제
    @Transactional
    public void deleteSchedule(Long id) {
        log.info("Deleting schedule with id: {}", id);

        // 일정 존재 여부 확인
        Schedule schedule = findScheduleById(id);

        // 일정 삭제
        scheduleRepository.delete(schedule);

        log.info("Schedule deleted successfully with id: {}", id);
    }

    // ID로 일정을 조회하는 private 메서드 - 일정이 존재하지 않을 경우 예외 발생
    private Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Schedule not found with id: {}", id);
                    return new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND);
                });
    }

}
