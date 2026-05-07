package com.schedule.dto;

import lombok.*;

/* 사용자 가입 요청 DTO */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSignupRequest {

    private String username; /* 사용자명 */
    private String password; /* 비밀번호 */
    private String email;    /* 이메일 */
}
