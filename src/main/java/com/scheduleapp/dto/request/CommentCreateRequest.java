package com.scheduleapp.dto.request;

import com.scheduleapp.entity.Comment;
import com.scheduleapp.entity.Schedule;
import com.scheduleapp.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequest {

    @NotNull(message = "유저 ID는 필수입니다.")
    private Long userId;

    @NotNull(message = "일정 ID는 필수입니다.")
    private Long scheduleId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    // DTO를 Entity로 변환
    public Comment toEntity(User user, Schedule schedule) {
        return Comment.builder()
                .content(content)
                .user(user)
                .schedule(schedule)
                .build();
    }
}
