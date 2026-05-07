package com.schedule.repository;

import com.schedule.entity.ScheduleDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/* 일정 날짜 Repository */
@Repository
public interface ScheduleDateRepository extends JpaRepository<ScheduleDateEntity, Integer> {

    List<ScheduleDateEntity> findByScheduleScheduleId(Integer scheduleId);

    long countByScheduleScheduleId(Integer scheduleId);

    Optional<ScheduleDateEntity> findByScheduleScheduleIdAndCandidateDate(Integer scheduleId, LocalDate candidateDate);
}
