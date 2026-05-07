package com.schedule.repository;

import com.schedule.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/* 사용자 Repository */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /* 사용자명으로 사용자 조회 */
    Optional<UserEntity> findByUsername(String username);

    /* 이메일로 사용자 조회 */
    Optional<UserEntity> findByEmail(String email);

    /* 사용자명 중복 확인 */
    boolean existsByUsername(String username);

    /* 이메일 중복 확인 */
    boolean existsByEmail(String email);
}
