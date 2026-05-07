package com.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/* 일정 엔티티 */
@Entity
@Table(name = "SCHEDULE", uniqueConstraints = {
    @UniqueConstraint(columnNames = "SHARE_CODE")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Integer scheduleId;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title; /* 일정 제목 (합주, 모임 등) */

    @Column(name = "SHARE_CODE", nullable = false, length = 20, unique = true)
    private String shareCode; /* 초대 링크 코드 */

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleDateEntity> dates;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleMemberEntity> members;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
