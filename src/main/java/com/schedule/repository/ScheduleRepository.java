package com.schedule.repository;

import com.schedule.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/* 일정 Repository */
@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {

    /* 공유 코드로 일정 조회 */
    Optional<ScheduleEntity> findByShareCode(String shareCode);

    /* 공유 코드 중복 확인 */
    boolean existsByShareCode(String shareCode);
}
