package com.schedule.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleCreateRequest {
    private String title;
    private List<String> members;
    private List<LocalDate> dates;
}
