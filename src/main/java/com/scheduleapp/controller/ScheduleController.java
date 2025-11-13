package com.scheduleapp.controller;

import com.scheduleapp.dto.request.ScheduleCreateRequest;
import com.scheduleapp.dto.request.ScheduleUpdateRequest;
import com.scheduleapp.dto.response.ScheduleResponse;
import com.scheduleapp.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성 API
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleCreateRequest request){
        log.info("POST /api/Schedules - Creating schedule");

        ScheduleResponse response = scheduleService.createSchedule(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 일정 단건 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(@PathVariable Long id) {
        log.info("GET /api/schedules/{} - Fetching schedule", id);

        ScheduleResponse response = scheduleService.getSchedule(id);

        return ResponseEntity.ok(response);
    }

    // 일정 전체 조회 API(최신순)
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        log.info("GET /api/schedules - Fetching all schedules");

        List<ScheduleResponse> responses = scheduleService.getAllSchedules();

        return ResponseEntity.ok(responses);
    }

    // 일정 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleUpdateRequest request){
        log.info("PUT /api/Schedules/{} - Updating schedule", id);
        ScheduleResponse response = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(response);
    }

    // 일정 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleResponse> deleteSchedule(@PathVariable Long id){
        log.info("DELETE /api/Schedules/{} - Deleting schedule", id);
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
