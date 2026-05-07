package com.schedule.controller;

import com.schedule.dto.UserResponseRequest;
import com.schedule.dto.VoteResultResponse;
import com.schedule.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/* 투표 컨트롤러 */
@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VoteController {

    private final VoteService voteService;

    /* 사용자 응답 제출 */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitResponse(
            @RequestBody UserResponseRequest request) {
        try {
            voteService.submitResponse(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "투표가 저장되었습니다");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /* 투표 결과 조회 */
    @GetMapping("/result/{scheduleId}")
    public ResponseEntity<Map<String, Object>> getVoteResult(@PathVariable Integer scheduleId) {
        try {
            VoteResultResponse result = voteService.getVoteResult(scheduleId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
