package com.schedule.repository;

import com.schedule.entity.ScheduleMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleMemberRepository extends JpaRepository<ScheduleMemberEntity, Integer> {
    List<ScheduleMemberEntity> findByScheduleScheduleId(Integer scheduleId);
    void deleteByScheduleScheduleId(Integer scheduleId);
}
