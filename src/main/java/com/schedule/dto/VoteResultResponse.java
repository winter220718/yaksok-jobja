package com.schedule.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/* 투표 결과 응답 DTO */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VoteResultResponse {

    private Integer scheduleId;                /* 일정 ID */
    private String title;                      /* 일정 제목 */
    private List<LocalDate> dates;             /* 후보 날짜 */
    private Map<LocalDate, DateResultDto> results; /* 날짜별 결과 */
    private LocalDate recommendedDate;         /* 추천 날짜 (가능한 사람이 가장 많은 날짜) */

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DateResultDto {
        private LocalDate date;                /* 날짜 */
        private int availableCount;            /* 가능한 사람 수 */
        private int unavailableCount;          /* 불가능한 사람 수 */
        private List<String> availableUsers;   /* 가능한 사용자 목록 */
        private List<String> unavailableUsers; /* 불가능한 사용자 목록 */
    }
}
