const API_BASE_URL = '/api';
let createdSchedule = null;

/* ======================================
   달력 상태
   ====================================== */

let selectedDates = new Set();
let calYear, calMonth;

$(document).ready(function() {
    const now = new Date();
    calYear = now.getFullYear();
    calMonth = now.getMonth();
    renderAdminCalendar();
});

function renderAdminCalendar() {
    $('#calHeaderTitle').text(calYear + '년 ' + (calMonth + 1) + '월');

    const grid = $('#adminCalGrid');
    grid.empty();

    const firstDay = new Date(calYear, calMonth, 1).getDay();
    const daysInMonth = new Date(calYear, calMonth + 1, 0).getDate();
    const prevDays = new Date(calYear, calMonth, 0).getDate();
    const todayStr = formatDateStr(new Date());

    for (let i = firstDay - 1; i >= 0; i--) {
        grid.append('<div class="cal-cell"><div class="cal-day other-month">' + (prevDays - i) + '</div></div>');
    }

    for (let d = 1; d <= daysInMonth; d++) {
        const ds = calYear + '-' + pad2(calMonth + 1) + '-' + pad2(d);
        const isSelected = selectedDates.has(ds);
        const isToday = ds === todayStr;
        let cls = 'cal-day';
        if (isSelected) {
            cls += ' selected';
        } else if (isToday) {
            cls += ' today selectable';
        } else {
            cls += ' selectable';
        }
        grid.append(
            '<div class="cal-cell">' +
                '<div class="' + cls + '" onclick="toggleDate(\'' + ds + '\')">' + d + '</div>' +
            '</div>'
        );
    }

    const total = firstDay + daysInMonth;
    const remaining = total % 7 === 0 ? 0 : 7 - (total % 7);
    for (let d = 1; d <= remaining; d++) {
        grid.append('<div class="cal-cell"><div class="cal-day other-month">' + d + '</div></div>');
    }

    updateSelectedDatesSummary();
}

function toggleDate(ds) {
    if (selectedDates.has(ds)) {
        selectedDates.delete(ds);
    } else {
        selectedDates.add(ds);
    }
    renderAdminCalendar();
}

function removeSelectedDate(ds) {
    selectedDates.delete(ds);
    renderAdminCalendar();
}

function updateSelectedDatesSummary() {
    const container = $('#selectedDatesSummary');
    container.empty();
    const sorted = Array.from(selectedDates).sort();
    if (sorted.length === 0) {
        container.html('<span style="color:var(--gray-400);font-size:0.85rem;">날짜를 클릭해 선택하세요</span>');
        return;
    }
    sorted.forEach(function(ds) {
        const d = new Date(ds + 'T00:00:00');
        const label = (d.getMonth() + 1) + '/' + d.getDate() + ' (' + ['일','월','화','수','목','금','토'][d.getDay()] + ')';
        container.append(
            '<span class="date-tag">' +
                label +
                '<span class="remove-tag" onclick="removeSelectedDate(\'' + ds + '\')">&times;</span>' +
            '</span>'
        );
    });
}

function calPrevMonth() {
    calMonth--;
    if (calMonth < 0) { calMonth = 11; calYear--; }
    renderAdminCalendar();
}

function calNextMonth() {
    calMonth++;
    if (calMonth > 11) { calMonth = 0; calYear++; }
    renderAdminCalendar();
}

/* ======================================
   약속 만들기
   ====================================== */

function createSchedule() {
    const title = $('#scheduleTitle').val().trim();
    if (!title) { showToast('약속 이름을 입력해주세요', 'error'); return; }

    const members = [];
    $('.member-input').each(function() {
        const v = $(this).val().trim();
        if (v) members.push(v);
    });
    if (members.length === 0) { showToast('멤버를 최소 1명 입력해주세요', 'error'); return; }

    const dates = Array.from(selectedDates).sort();
    if (dates.length === 0) { showToast('후보 날짜를 최소 1개 선택해주세요', 'error'); return; }

    $.ajax({
        url: API_BASE_URL + '/schedule/create',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ title, members, dates }),
        success: function(res) {
            if (res.success) {
                createdSchedule = res.data;
                const url = window.location.origin + '/vote.html?code=' + res.data.shareCode;
                $('#shareUrlDisplay').text(url);
                $('#createSection').addClass('hidden');
                $('#resultSection').removeClass('hidden');
            }
        },
        error: function(xhr) {
            showToast(xhr.responseJSON?.message || '약속 생성에 실패했습니다', 'error');
        }
    });
}

function copyShareLink() {
    const url = $('#shareUrlDisplay').text();
    navigator.clipboard.writeText(url).then(function() {
        showToast('링크가 복사되었습니다!', 'success');
    }).catch(function() {
        showToast('복사에 실패했습니다', 'error');
    });
}

function goToVotePage() {
    window.open($('#shareUrlDisplay').text(), '_blank');
}

function resetForm() {
    $('#createSection').removeClass('hidden');
    $('#resultSection').addClass('hidden');
    $('#scheduleTitle').val('');
    resetMemberInputs();
    selectedDates = new Set();
    renderAdminCalendar();
    createdSchedule = null;
}

/* ======================================
   멤버 입력 관리
   ====================================== */

function addMember() {
    $('#membersContainer').append(
        '<div class="date-input-group">' +
            '<input type="text" class="form-control member-input" placeholder="이름 입력">' +
            '<button type="button" class="btn btn-danger btn-sm" onclick="removeMember(this)">삭제</button>' +
        '</div>'
    );
}

function removeMember(btn) {
    if ($('.member-input').length > 1) {
        $(btn).parent().remove();
    } else {
        showToast('멤버는 최소 1명이어야 합니다', 'error');
    }
}

function resetMemberInputs() {
    $('#membersContainer').html(
        '<div class="date-input-group">' +
            '<input type="text" class="form-control member-input" placeholder="이름 입력">' +
            '<button type="button" class="btn btn-danger btn-sm" onclick="removeMember(this)">삭제</button>' +
        '</div>'
    );
}

/* ======================================
   유틸리티
   ====================================== */

function pad2(n) { return n < 10 ? '0' + n : String(n); }

function formatDateStr(d) {
    return d.getFullYear() + '-' + pad2(d.getMonth() + 1) + '-' + pad2(d.getDate());
}

function showToast(message, type) {
    type = type || 'info';
    const toast = $('#toast');
    toast.text(message).removeClass('hidden success error').addClass(type);
    setTimeout(function() { toast.addClass('hidden'); }, 3000);
}
