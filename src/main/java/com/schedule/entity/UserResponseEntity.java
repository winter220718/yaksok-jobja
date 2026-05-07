package com.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/* 사용자 응답 엔티티 */
@Entity
@Table(name = "USER_RESPONSE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESPONSE_ID")
    private Integer responseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATE_ID", nullable = false)
    private ScheduleDateEntity scheduleDate;

    @Column(name = "USER_NAME", nullable = false, length = 50)
    private String userName; /* 익명 또는 실제 이름 */

    @Column(name = "IS_AVAILABLE", nullable = false)
    private Boolean isAvailable; /* true: 가능, false: 불가능 */

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
