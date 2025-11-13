package com.scheduleapp.dto.request;

import com.scheduleapp.entity.Schedule;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "작성자명은 필수 입니다.")
    @Size(max = 50, message = "작성자명은 50자 이하여야 합니다.")
    private String username;

    @NotBlank(message = "할일 제목은 필수입니다.")
    @Size(max = 200, message = "할일 제목은 200자 이하여야 합니다.")
    private String title;

    private String content;

    // DTO를 Entity로 변환
    public Schedule toEntity() {
        return Schedule.builder()
                .username(username)
                .title(title)
                .content(content)
                .build();
    }
}
