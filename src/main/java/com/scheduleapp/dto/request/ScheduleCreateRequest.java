package com.scheduleapp.dto.request;

import com.scheduleapp.entity.Schedule;
import com.scheduleapp.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleCreateRequest {

    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "할일 제목은 필수입니다.")
    @Size(max = 200, message = "할일 제목은 200자 이하여야 합니다.")
    private String title;

    private String content;

    // DTO를 Entity로 변환
    public Schedule toEntity(User user) {
        return Schedule.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }
}
