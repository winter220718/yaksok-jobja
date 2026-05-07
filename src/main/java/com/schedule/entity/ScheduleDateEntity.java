package com.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/* 일정 후보 날짜 엔티티 */
@Entity
@Table(name = "SCHEDULE_DATE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DATE_ID")
    private Integer dateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID", nullable = false)
    private ScheduleEntity schedule;

    @Column(name = "CANDIDATE_DATE", nullable = false, columnDefinition = "DATE")
    private LocalDate candidateDate; /* 후보 날짜 */

    @OneToMany(mappedBy = "scheduleDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserResponseEntity> responses;

    @Column(name = "CREATED_AT", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
