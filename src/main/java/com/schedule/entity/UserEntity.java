package com.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/* 사용자 엔티티 */
@Entity
@Table(name = "USERS", uniqueConstraints = {
    @UniqueConstraint(columnNames = "USERNAME"),
    @UniqueConstraint(columnNames = "EMAIL")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password; /* 암호화된 비밀번호 */

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

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
