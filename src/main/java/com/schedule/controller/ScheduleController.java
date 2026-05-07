package com.schedule.controller;

import com.schedule.dto.ScheduleCreateRequest;
import com.schedule.dto.ScheduleResponse;
import com.schedule.dto.ScheduleUpdateRequest;
import com.schedule.entity.ScheduleEntity;
import com.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /* 약속 생성 */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSchedule(@RequestBody ScheduleCreateRequest request) {
        try {
            ScheduleResponse schedule = scheduleService.createSchedule(request);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("data", schedule);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /* 전체 목록 조회 (관리자용) */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllSchedules() {
        try {
            List<ScheduleResponse> list = scheduleService.findAll();
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("data", list);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /* 공유 코드로 조회 */
    @GetMapping("/code/{shareCode}")
    public ResponseEntity<Map<String, Object>> getScheduleByCode(@PathVariable String shareCode) {
        try {
            ScheduleEntity schedule = scheduleService.findByShareCode(shareCode)
                .orElseThrow(() -> new IllegalArgumentException("약속을 찾을 수 없습니다"));
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("data", scheduleService.convertToResponse(schedule));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /* 약속 수정 */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateSchedule(
            @PathVariable Integer scheduleId,
            @RequestBody ScheduleUpdateRequest request) {
        try {
            ScheduleResponse updated = scheduleService.updateSchedule(scheduleId, request);
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("data", updated);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private ResponseEntity<Map<String, Object>> error(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", false);
        res.put("message", message);
        return ResponseEntity.badRequest().body(res);
    }
}
