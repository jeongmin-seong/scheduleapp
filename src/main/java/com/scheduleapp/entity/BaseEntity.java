package com.scheduleapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
public abstract class BaseEntity {

    // 생성일 - Entity 생성후 저장시 자동으로 현재 시간 저장
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 수정일 - Entity 수정시 자동으로 현재 시간 저장
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
