package com.scheduleapp.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleUpdateRequest {

    @Size(max = 200, message = "할일 제목은 200자 이하여야 합니다.")
    private String title;

    private String content;

}
