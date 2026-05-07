package com.schedule.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FreeVoteRequest {
    private Integer scheduleId;
    private String userName;
    private List<LocalDate> availableDates;
}
