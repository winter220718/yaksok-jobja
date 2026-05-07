package com.schedule.service;

import com.schedule.dto.FreeVoteRequest;
import com.schedule.dto.UserResponseRequest;
import com.schedule.dto.VoteResultResponse;
import com.schedule.entity.ScheduleDateEntity;
import com.schedule.entity.ScheduleEntity;
import com.schedule.entity.UserResponseEntity;
import com.schedule.repository.ScheduleDateRepository;
import com.schedule.repository.ScheduleRepository;
import com.schedule.repository.UserResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/* 투표 서비스 */
@Service
@RequiredArgsConstructor
public class VoteService {

    private final UserResponseRepository userResponseRepository;
    private final ScheduleDateRepository scheduleDateRepository;
    private final ScheduleRepository scheduleRepository;

    /* 사용자 응답 저장 또는 업데이트 */
    @Transactional
    public void submitResponse(UserResponseRequest request) {
        ScheduleDateEntity scheduleDate = scheduleDateRepository.findById(request.getDateId())
            .orElseThrow(() -> new IllegalArgumentException("날짜를 찾을 수 없습니다"));

        /* 기존 응답 확인 */
        Optional<UserResponseEntity> existingResponse = userResponseRepository
            .findByScheduleDateDateIdAndUserName(request.getDateId(), request.getUserName());

        if (existingResponse.isPresent()) {
            /* 기존 응답 업데이트 */
            UserResponseEntity response = existingResponse.get();
            response.setIsAvailable(request.getIsAvailable());
            userResponseRepository.save(response);
        } else {
            /* 새로운 응답 저장 */
            UserResponseEntity response = new UserResponseEntity();
            response.setScheduleDate(scheduleDate);
            response.setUserName(request.getUserName());
            response.setIsAvailable(request.getIsAvailable());
            userResponseRepository.save(response);
        }
    }

    /* 투표 결과 조회 */
    public VoteResultResponse getVoteResult(Integer scheduleId) {
        List<UserResponseEntity> allResponses = userResponseRepository.findByScheduleId(scheduleId);
        List<ScheduleDateEntity> dates = scheduleDateRepository.findByScheduleScheduleId(scheduleId);

        Map<LocalDate, VoteResultResponse.DateResultDto> results = new LinkedHashMap<>();
        LocalDate recommendedDate = null;
        int maxAvailableCount = -1;

        /* 각 날짜별로 결과 집계 */
        for (ScheduleDateEntity date : dates) {
            LocalDate candidateDate = date.getCandidateDate();
            List<UserResponseEntity> responsesForDate = allResponses.stream()
                .filter(r -> r.getScheduleDate().getDateId().equals(date.getDateId()))
                .collect(Collectors.toList());

            List<String> availableUsers = new ArrayList<>();
            List<String> unavailableUsers = new ArrayList<>();

            for (UserResponseEntity response : responsesForDate) {
                if (response.getIsAvailable()) {
                    availableUsers.add(response.getUserName());
                } else {
                    unavailableUsers.add(response.getUserName());
                }
            }

            VoteResultResponse.DateResultDto dateResult = new VoteResultResponse.DateResultDto();
            dateResult.setDate(candidateDate);
            dateResult.setAvailableCount(availableUsers.size());
            dateResult.setUnavailableCount(unavailableUsers.size());
            dateResult.setAvailableUsers(availableUsers);
            dateResult.setUnavailableUsers(unavailableUsers);

            results.put(candidateDate, dateResult);

            /* 추천 날짜 결정 */
            if (availableUsers.size() > maxAvailableCount) {
                maxAvailableCount = availableUsers.size();
                recommendedDate = candidateDate;
            }
        }

        VoteResultResponse response = new VoteResultResponse();
        response.setScheduleId(scheduleId);
        response.setDates(dates.stream()
            .map(ScheduleDateEntity::getCandidateDate)
            .sorted()
            .collect(Collectors.toList()));
        response.setResults(results);
        response.setRecommendedDate(recommendedDate);

        return response;
    }

    /* FREE 모드: 멤버가 가능한 날짜 목록으로 한번에 제출 */
    @Transactional
    public void submitFreeResponse(FreeVoteRequest request) {
        ScheduleEntity schedule = scheduleRepository.findById(request.getScheduleId())
            .orElseThrow(() -> new IllegalArgumentException("약속을 찾을 수 없습니다"));

        userResponseRepository.deleteByScheduleIdAndUserName(request.getScheduleId(), request.getUserName());

        if (request.getAvailableDates() == null) return;

        for (LocalDate date : request.getAvailableDates()) {
            ScheduleDateEntity dateEntity = scheduleDateRepository
                .findByScheduleScheduleIdAndCandidateDate(request.getScheduleId(), date)
                .orElseGet(() -> {
                    ScheduleDateEntity newDate = new ScheduleDateEntity();
                    newDate.setSchedule(schedule);
                    newDate.setCandidateDate(date);
                    newDate.setCreatedAt(LocalDateTime.now());
                    return scheduleDateRepository.save(newDate);
                });

            UserResponseEntity response = new UserResponseEntity();
            response.setScheduleDate(dateEntity);
            response.setUserName(request.getUserName());
            response.setIsAvailable(true);
            userResponseRepository.save(response);
        }
    }

    /* 날짜별 가능한 응답 개수 */
    public long getAvailableCount(Integer dateId) {
        return userResponseRepository.countByScheduleDateDateIdAndIsAvailableTrue(dateId);
    }

    /* 날짜별 불가능한 응답 개수 */
    public long getUnavailableCount(Integer dateId) {
        return userResponseRepository.countByScheduleDateDateIdAndIsAvailableFalse(dateId);
    }
}
