package com.schedule.repository;

import com.schedule.entity.UserResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/* 사용자 응답 Repository */
@Repository
public interface UserResponseRepository extends JpaRepository<UserResponseEntity, Integer> {

    /* 날짜별 모든 응답 조회 */
    List<UserResponseEntity> findByScheduleDateDateId(Integer dateId);

    /* 사용자 응답 조회 (이름 기반) */
    Optional<UserResponseEntity> findByScheduleDateDateIdAndUserName(Integer dateId, String userName);

    /* 날짜별 가능한 응답 개수 조회 */
    long countByScheduleDateDateIdAndIsAvailableTrue(Integer dateId);

    /* 날짜별 불가능한 응답 개수 조회 */
    long countByScheduleDateDateIdAndIsAvailableFalse(Integer dateId);

    /* 일정별 모든 응답 조회 */
    @Query("SELECT ur FROM UserResponseEntity ur " +
           "WHERE ur.scheduleDate.schedule.scheduleId = :scheduleId " +
           "ORDER BY ur.scheduleDate.candidateDate, ur.userName")
    List<UserResponseEntity> findByScheduleId(@Param("scheduleId") Integer scheduleId);

    /* FREE 모드 재투표시 기존 응답 삭제 */
    @Query("DELETE FROM UserResponseEntity ur " +
           "WHERE ur.scheduleDate.schedule.scheduleId = :scheduleId " +
           "AND ur.userName = :userName")
    @org.springframework.data.jpa.repository.Modifying
    void deleteByScheduleIdAndUserName(@Param("scheduleId") Integer scheduleId,
                                       @Param("userName") String userName);
}
