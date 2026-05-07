package com.schedule.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleResponse {

    private Integer scheduleId;
    private String title;
    private String shareCode;
    private String voteMode;
    private List<String> members;
    private List<DateInfo> dates;
    private String createdAt;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DateInfo {
        private Integer dateId;
        private LocalDate date;
    }
}
