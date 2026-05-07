package com.schedule.dto;

import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleUpdateRequest {
    private String title;
    private List<String> members;
}
