package com.schedule.dto;

import lombok.*;

/* 사용자 로그인 요청 DTO */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginRequest {

    private String username; /* 사용자명 */
    private String password; /* 비밀번호 */
}
