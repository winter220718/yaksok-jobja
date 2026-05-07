package com.schedule.service;

import com.schedule.dto.UserSignupRequest;
import com.schedule.entity.UserEntity;
import com.schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

/* 사용자 서비스 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 사용자 가입 */
    public UserEntity signup(UserSignupRequest request) {
        /* 사용자명 중복 확인 */
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다");
        }

        /* 이메일 중복 확인 */
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    /* 사용자명으로 사용자 조회 */
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /* ID로 사용자 조회 */
    public Optional<UserEntity> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    /* 비밀번호 검증 */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
