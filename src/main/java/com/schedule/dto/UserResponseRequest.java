package com.schedule.dto;

import lombok.*;

/* 사용자 투표 요청 DTO */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseRequest {

    private Integer dateId;       /* 날짜 ID */
    private String userName;      /* 사용자 이름 */
    private Boolean isAvailable;  /* 가능 여부 */
}
