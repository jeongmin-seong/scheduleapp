package com.scheduleapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment extends BaseEntity{

    // 고유 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 작성 유저 (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 일정 (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    // 생성자
    @Builder
    public Comment(String content, User user, Schedule schedule) {
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }

    // 댓글 수정 메서드
    public void update(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }

    // 작성자명 조회 편의 메서드
    public String getUsername() {
        return user.getUsername();
    }

    // 유저 ID 조회 편의 메서드
    public Long getUserId() {
        return user.getId();
    }

    // 일정 ID 조회 편의 메서드
    public Long getScheduleId() {
        return schedule.getId();
    }
}
