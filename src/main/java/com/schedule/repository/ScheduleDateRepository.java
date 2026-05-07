package com.schedule.repository;

import com.schedule.entity.ScheduleDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/* 일정 날짜 Repository */
@Repository
public interface ScheduleDateRepository extends JpaRepository<ScheduleDateEntity, Integer> {

    /* 일정별 모든 날짜 조회 */
    List<ScheduleDateEntity> findByScheduleScheduleId(Integer scheduleId);

    /* 일정별 날짜 개수 조회 */
    long countByScheduleScheduleId(Integer scheduleId);
}
