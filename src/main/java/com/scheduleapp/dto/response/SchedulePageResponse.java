package com.scheduleapp.dto.response;

import com.scheduleapp.entity.Comment;
import com.scheduleapp.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedulePageResponse {

    // 일정 고유 식별자 - PK
    private Long id;

    // 일정 제목
    private String title;

    // 일정 내용
    private String content;

    // 댓글 개수
    private Long commentCount;

    // 일정 작성일
    private LocalDateTime createdAt;

    // 일절 수정일
    private LocalDateTime updatedAt;

    // 일정 작성 유저명
    private String username;

    // 정적 팩토리 메서드 - Entity와 댓글 개수를 DTO로 변환
    public static SchedulePageResponse of(Schedule schedule, Long commentCount) {
        return SchedulePageResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .commentCount(commentCount)
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .username(schedule.getUsername())
                .build();
    }

}
