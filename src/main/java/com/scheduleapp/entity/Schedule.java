package com.scheduleapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedules")
public class Schedule extends  BaseEntity {

    // 일정 고유 식별자(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자명(연관관계)
    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩 설정
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 일정 제목
    @Column(nullable = false, length = 200)
    private String title;

    // 일정 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 생성자
    @Builder
    public Schedule(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    // 일정 수정 메서드
    public void update(String title, String content) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (content != null) {
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
}
