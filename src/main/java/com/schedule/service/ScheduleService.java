package com.schedule.service;

import com.schedule.dto.ScheduleCreateRequest;
import com.schedule.dto.ScheduleResponse;
import com.schedule.dto.ScheduleUpdateRequest;
import com.schedule.entity.ScheduleDateEntity;
import com.schedule.entity.ScheduleEntity;
import com.schedule.entity.ScheduleMemberEntity;
import com.schedule.repository.ScheduleDateRepository;
import com.schedule.repository.ScheduleMemberRepository;
import com.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDateRepository scheduleDateRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        String shareCode = generateUniqueShareCode();

        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setTitle(request.getTitle());
        schedule.setShareCode(shareCode);
        schedule.setVoteMode(request.getVoteMode() != null ? request.getVoteMode() : "RESTRICTED");
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());

        ScheduleEntity saved = scheduleRepository.save(schedule);

        /* 멤버 저장 */
        if (request.getMembers() != null) {
            List<ScheduleMemberEntity> members = request.getMembers().stream()
                .filter(name -> name != null && !name.isBlank())
                .map(name -> {
                    ScheduleMemberEntity m = new ScheduleMemberEntity();
                    m.setSchedule(saved);
                    m.setMemberName(name.trim());
                    return m;
                })
                .collect(Collectors.toList());
            scheduleMemberRepository.saveAll(members);
        }

        /* 후보 날짜 저장 */
        if (request.getDates() != null) {
            List<ScheduleDateEntity> dates = request.getDates().stream()
                .map(candidateDate -> {
                    ScheduleDateEntity d = new ScheduleDateEntity();
                    d.setSchedule(saved);
                    d.setCandidateDate(candidateDate);
                    d.setCreatedAt(LocalDateTime.now());
                    return d;
                })
                .collect(Collectors.toList());
            scheduleDateRepository.saveAll(dates);
        }

        return convertToResponse(saved);
    }

    /* 전체 목록 */
    public List<ScheduleResponse> findAll() {
        return scheduleRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /* 약속 수정 (제목 + 멤버 교체) */
    @Transactional
    public ScheduleResponse updateSchedule(Integer scheduleId, ScheduleUpdateRequest request) {
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("약속을 찾을 수 없습니다"));

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            schedule.setTitle(request.getTitle().trim());
        }
        schedule.setUpdatedAt(LocalDateTime.now());
        scheduleRepository.save(schedule);

        if (request.getMembers() != null) {
            scheduleMemberRepository.deleteByScheduleScheduleId(scheduleId);
            List<ScheduleMemberEntity> members = request.getMembers().stream()
                .filter(name -> name != null && !name.isBlank())
                .map(name -> {
                    ScheduleMemberEntity m = new ScheduleMemberEntity();
                    m.setSchedule(schedule);
                    m.setMemberName(name.trim());
                    return m;
                })
                .collect(Collectors.toList());
            scheduleMemberRepository.saveAll(members);
        }

        return convertToResponse(schedule);
    }

    public Optional<ScheduleEntity> findByShareCode(String shareCode) {
        return scheduleRepository.findByShareCode(shareCode);
    }

    public Optional<ScheduleEntity> findById(Integer scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }

    private String generateUniqueShareCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (scheduleRepository.existsByShareCode(code));
        return code;
    }

    public ScheduleResponse convertToResponse(ScheduleEntity schedule) {
        List<String> members = scheduleMemberRepository
            .findByScheduleScheduleId(schedule.getScheduleId())
            .stream()
            .map(ScheduleMemberEntity::getMemberName)
            .collect(Collectors.toList());

        List<ScheduleResponse.DateInfo> dates = scheduleDateRepository
            .findByScheduleScheduleId(schedule.getScheduleId())
            .stream()
            .sorted((a, b) -> a.getCandidateDate().compareTo(b.getCandidateDate()))
            .map(d -> new ScheduleResponse.DateInfo(d.getDateId(), d.getCandidateDate()))
            .collect(Collectors.toList());

        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(schedule.getScheduleId());
        response.setTitle(schedule.getTitle());
        response.setShareCode(schedule.getShareCode());
        response.setVoteMode(schedule.getVoteMode() != null ? schedule.getVoteMode() : "RESTRICTED");
        response.setMembers(members);
        response.setDates(dates);
        response.setCreatedAt(schedule.getCreatedAt().toString());
        return response;
    }
}
