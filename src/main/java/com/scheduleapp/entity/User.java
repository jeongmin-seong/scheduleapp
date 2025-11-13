package com.scheduleapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity{

    // 유저 고유 식별자(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저명
    @Column(nullable = false, length = 50)
    private String username;

    // 이메일 (unique)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // 비밀번호 (암호화되어 저장)
    @Column(nullable = false)
    private String password;

    // 생성자
    @Builder
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 유저 정보 수정 메서드
    public void update(String username, String email) {
        if (username != null && !username.isBlank()) {
            this.username = username;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
    }

    // 비밀번호 변경 메서드
    public void changePassword(String newPassword) {
        this.password = newPassword;    }

}
