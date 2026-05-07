const API_BASE_URL = '/api';
let createdSchedule = null;

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

    const dates = [];
    $('.date-input').each(function() {
        const v = $(this).val();
        if (v) dates.push(v);
    });
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
    resetDateInputs();
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
   날짜 입력 관리
   ====================================== */

function addDate() {
    $('#datesContainer').append(
        '<div class="date-input-group">' +
            '<input type="date" class="date-input">' +
            '<button type="button" class="btn btn-danger btn-sm" onclick="removeDate(this)">삭제</button>' +
        '</div>'
    );
}

function removeDate(btn) {
    if ($('.date-input').length > 1) {
        $(btn).parent().remove();
    } else {
        showToast('날짜는 최소 1개이어야 합니다', 'error');
    }
}

function resetDateInputs() {
    $('#datesContainer').html(
        '<div class="date-input-group">' +
            '<input type="date" class="date-input">' +
            '<button type="button" class="btn btn-danger btn-sm" onclick="removeDate(this)">삭제</button>' +
        '</div>'
    );
}

/* ======================================
   토스트
   ====================================== */

function showToast(message, type) {
    type = type || 'info';
    const toast = $('#toast');
    toast.text(message).removeClass('hidden success error').addClass(type);
    setTimeout(function() { toast.addClass('hidden'); }, 3000);
}
