const API_BASE_URL = '/api';
let currentSchedule = null;
let selectedMember = null;
let availableDates = new Set();

$(document).ready(function() {
    const code = getURLParam('code');
    if (!code) {
        showError('유효하지 않은 링크입니다');
        $('#loadingSpinner').addClass('hidden');
        return;
    }
    loadSchedule(code);
});

/* ======================================
   일정 로드
   ====================================== */

function loadSchedule(code) {
    $.ajax({
        url: API_BASE_URL + '/schedule/code/' + code,
        method: 'GET',
        success: function(res) {
            $('#loadingSpinner').addClass('hidden');
            if (res.success) {
                currentSchedule = res.data;
                renderMemberSelect();
            } else {
                showError('약속을 찾을 수 없습니다');
            }
        },
        error: function() {
            $('#loadingSpinner').addClass('hidden');
            showError('약속을 불러오지 못했습니다');
        }
    });
}

/* ======================================
   멤버 선택 화면
   ====================================== */

function renderMemberSelect() {
    $('#scheduleTitleDisplay').text(currentSchedule.title);

    const container = $('#memberButtons');
    container.empty();

    if (!currentSchedule.members || currentSchedule.members.length === 0) {
        container.html('<p class="empty-message">등록된 멤버가 없습니다</p>');
    } else {
        currentSchedule.members.forEach(function(name) {
            container.append(
                '<button class="member-btn" onclick="selectMember(\'' + escapeHtml(name) + '\')">' +
                    name +
                '</button>'
            );
        });
    }

    container.append(
        '<div style="grid-column:1/-1;text-align:center;margin-top:1rem;">' +
            '<button class="btn btn-secondary btn-sm" onclick="loadAndShowResults()">📊 현재 결과 보기</button>' +
        '</div>'
    );

    $('#memberSelectSection').removeClass('hidden');
}

function selectMember(name) {
    selectedMember = name;
    availableDates = new Set();

    $('#voteTitleDisplay').text(currentSchedule.title);
    $('#voteNameDisplay').text(name + '님의 투표');

    renderVoteCalendars();

    $('#memberSelectSection').addClass('hidden');
    $('#resultSection').addClass('hidden');
    $('#voteSection').removeClass('hidden');
}

function backToMemberSelect() {
    selectedMember = null;
    availableDates = new Set();
    $('#voteSection').addClass('hidden');
    $('#resultSection').addClass('hidden');
    $('#memberSelectSection').removeClass('hidden');
}

/* ======================================
   투표 달력
   ====================================== */

function renderVoteCalendars() {
    const months = {};
    currentSchedule.dates.forEach(function(dateInfo) {
        const ym = dateInfo.date.substring(0, 7);
        if (!months[ym]) months[ym] = [];
        months[ym].push(dateInfo);
    });

    const container = $('#voteCalendars');
    container.empty();

    Object.keys(months).sort().forEach(function(ym) {
        const parts = ym.split('-');
        const year = parseInt(parts[0]);
        const monthIdx = parseInt(parts[1]) - 1;
        container.append(renderVoteMonth(year, monthIdx, months[ym]));
    });
}

function renderVoteMonth(year, monthIdx, candidateDateInfos) {
    const candidateSet = {};
    candidateDateInfos.forEach(function(di) { candidateSet[di.date] = di.dateId; });

    const monthName = year + '년 ' + (monthIdx + 1) + '월';
    const firstDay = new Date(year, monthIdx, 1).getDay();
    const daysInMonth = new Date(year, monthIdx + 1, 0).getDate();
    const prevDays = new Date(year, monthIdx, 0).getDate();

    const weekdaysHtml = ['일','월','화','수','목','금','토'].map(function(d) {
        return '<div class="cal-weekday">' + d + '</div>';
    }).join('');

    let cells = '';

    for (let i = firstDay - 1; i >= 0; i--) {
        cells += '<div class="cal-cell"><div class="cal-day other-month">' + (prevDays - i) + '</div></div>';
    }

    for (let d = 1; d <= daysInMonth; d++) {
        const ds = year + '-' + pad2(monthIdx + 1) + '-' + pad2(d);
        if (candidateSet[ds] !== undefined) {
            const cls = 'cal-day ' + (availableDates.has(ds) ? 'available' : 'candidate');
            cells += '<div class="cal-cell"><div class="' + cls + '" onclick="toggleVoteDate(\'' + ds + '\')">' + d + '</div></div>';
        } else {
            cells += '<div class="cal-cell"><div class="cal-day non-candidate">' + d + '</div></div>';
        }
    }

    const total = firstDay + daysInMonth;
    const remaining = total % 7 === 0 ? 0 : 7 - (total % 7);
    for (let d = 1; d <= remaining; d++) {
        cells += '<div class="cal-cell"><div class="cal-day other-month">' + d + '</div></div>';
    }

    return '<div class="vote-month-block">' +
        '<div class="cal-widget">' +
            '<div class="cal-header"><div class="cal-header-title" style="margin:0 auto;">' + monthName + '</div></div>' +
            '<div class="cal-weekdays">' + weekdaysHtml + '</div>' +
            '<div class="cal-grid">' + cells + '</div>' +
        '</div>' +
    '</div>';
}

function toggleVoteDate(ds) {
    if (availableDates.has(ds)) {
        availableDates.delete(ds);
    } else {
        availableDates.add(ds);
    }
    renderVoteCalendars();
}

/* ======================================
   투표 제출
   ====================================== */

function submitVotes() {
    if (!selectedMember) return;

    const requests = currentSchedule.dates.map(function(dateInfo) {
        return $.ajax({
            url: API_BASE_URL + '/vote/submit',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                dateId: dateInfo.dateId,
                userName: selectedMember,
                isAvailable: availableDates.has(dateInfo.date)
            })
        });
    });

    $.when.apply($, requests)
        .done(function() {
            showToast('투표가 완료되었습니다!', 'success');
            loadAndShowResults();
        })
        .fail(function() {
            showToast('투표 제출에 실패했습니다', 'error');
        });
}

/* ======================================
   결과 표시
   ====================================== */

function loadAndShowResults() {
    $.ajax({
        url: API_BASE_URL + '/vote/result/' + currentSchedule.scheduleId,
        method: 'GET',
        success: function(res) {
            if (res.success) {
                renderResults(res.data);
                $('#voteSection').addClass('hidden');
                $('#memberSelectSection').addClass('hidden');
                $('#resultSection').removeClass('hidden');
            }
        },
        error: function() {
            showToast('결과를 불러오지 못했습니다', 'error');
        }
    });
}

function renderResults(result) {
    const container = $('#resultContent');
    container.empty();

    if (result.recommendedDate) {
        container.append(
            '<div class="result-card" style="border-color:#F59E0B;background:#FFFBEB;">' +
                '<div class="result-date">🎯 최적 날짜</div>' +
                '<div style="font-size:1.4rem;font-weight:700;color:#D97706;">' + formatDate(result.recommendedDate) + '</div>' +
            '</div>'
        );
    }

    result.dates.forEach(function(date) {
        const dr = result.results[date];
        const availBadges = dr.availableUsers.map(function(u) {
            return '<span class="user-badge available">' + escapeHtml(u) + '</span>';
        }).join('') || '<span style="color:var(--gray-400);font-size:0.85rem;">없음</span>';

        const unavailBadges = dr.unavailableUsers.map(function(u) {
            return '<span class="user-badge unavailable">' + escapeHtml(u) + '</span>';
        }).join('') || '<span style="color:var(--gray-400);font-size:0.85rem;">없음</span>';

        container.append(
            '<div class="result-card">' +
                '<div class="result-date">' + formatDate(date) + '</div>' +
                '<div class="result-stats">' +
                    '<div class="stat"><div class="stat-label">가능</div><div class="stat-value available">' + dr.availableCount + '</div></div>' +
                    '<div class="stat"><div class="stat-label">불가능</div><div class="stat-value unavailable">' + dr.unavailableCount + '</div></div>' +
                '</div>' +
                '<div class="result-users" style="margin-bottom:0.5rem;">' +
                    '<div class="result-users-label">✓ 가능</div><div>' + availBadges + '</div>' +
                '</div>' +
                '<div class="result-users">' +
                    '<div class="result-users-label">✗ 불가능</div><div>' + unavailBadges + '</div>' +
                '</div>' +
            '</div>'
        );
    });
}

/* ======================================
   유틸리티
   ====================================== */

function pad2(n) { return n < 10 ? '0' + n : String(n); }

function formatDate(dateString) {
    const d = new Date(dateString + 'T00:00:00');
    return d.toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'short' });
}

function getURLParam(name) {
    return new URLSearchParams(window.location.search).get(name);
}

function escapeHtml(str) {
    return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}

function showError(msg) {
    $('#errorMessage').text(msg).removeClass('hidden');
}

function showToast(message, type) {
    type = type || 'info';
    const toast = $('#toast');
    toast.text(message).removeClass('hidden success error').addClass(type);
    setTimeout(function() { toast.addClass('hidden'); }, 3000);
}
