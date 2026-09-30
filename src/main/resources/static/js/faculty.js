/**
 * SmartAttend - Faculty Portal Controller
 */

let activeSessions = [];
let currentMarkingSessionId = null;
let currentSessionRecords = [];

document.addEventListener('DOMContentLoaded', async () => {
    SmartAttendAPI.initTheme();

    const user = SmartAttendAPI.getUser();
    if (!user) {
        window.location.href = '/login.html';
        return;
    }

    const nameEls = document.querySelectorAll('.faculty-name-display');
    nameEls.forEach(el => el.textContent = user.fullName || user.name || user.username || 'Faculty Member');

    await loadInitialData();
    await loadActiveSessions();
});

async function loadInitialData() {
    // 0. Set default date
    const dateInput = document.getElementById('modal-date-input');
    if (dateInput) {
        dateInput.value = new Date().toISOString().split('T')[0];
    }

    // 1. Load subjects for session creation dropdown
    try {
        const subRes = await SmartAttendAPI.get('/subjects');
        const select = document.getElementById('modal-subject-select');
        if (select) {
            select.innerHTML = '';
            const subjects = (subRes.data && subRes.data.content) ? subRes.data.content : (subRes.data || []);
            subjects.forEach(s => {
                const opt = document.createElement('option');
                opt.value = s.id;
                opt.textContent = `${s.courseCode} — ${s.subjectName} (${s.subjectType})`;
                select.appendChild(opt);
            });
        }
    } catch (e) {
        console.warn('Error loading subjects:', e);
    }

    // 2. Load sections dropdown
    try {
        const secRes = await SmartAttendAPI.get('/academic/sections');
        const select = document.getElementById('modal-section-select');
        if (select) {
            select.innerHTML = '';
            const sections = (secRes.data && secRes.data.content) ? secRes.data.content : (secRes.data || []);
            sections.forEach(sec => {
                const opt = document.createElement('option');
                opt.value = sec.id;
                opt.textContent = `${sec.name} (${sec.department ? sec.department.name : 'CSE'})`;
                select.appendChild(opt);
            });
        }
    } catch (e) {
        console.warn('Error loading sections:', e);
    }
}

async function loadActiveSessions() {
    try {
        const res = await SmartAttendAPI.get('/attendance/sessions');
        activeSessions = (res.data && res.data.content) ? res.data.content : (res.data || []);
        renderSessionsTable(activeSessions);

        if (activeSessions.length > 0 && !currentMarkingSessionId) {
            openSessionForMarking(activeSessions[0].id);
        }
    } catch (err) {
        console.error('Error fetching faculty sessions:', err);
    }
}

function renderSessionsTable(sessions) {
    const tbody = document.getElementById('sessions-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';
    if (sessions.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center py-4 text-muted">No attendance sessions created today. Click [Start New Session] above.</td></tr>';
        return;
    }

    sessions.forEach(s => {
        const row = document.createElement('tr');
        const isLocked = s.isLocked;
        const statusBadge = isLocked ?
            '<span class="badge bg-secondary"><i class="fas fa-lock me-1"></i> LOCKED</span>' :
            '<span class="badge bg-success"><i class="fas fa-circle-dot me-1"></i> ACTIVE</span>';

        const pct = parseFloat(s.attendancePercentage || 0).toFixed(1);

        row.innerHTML = `
            <td><strong>${s.sessionCode}</strong></td>
            <td>${s.courseCode} — ${s.subjectName}</td>
            <td>${s.sectionName || 'Sec A'}</td>
            <td>Period ${s.periodNumber || 1}</td>
            <td><span class="fw-bold">${s.presentCount} / ${s.totalStudents}</span> (${pct}%)</td>
            <td>${statusBadge}</td>
            <td class="text-end">
                <button class="btn btn-sm btn-erp-primary py-1 px-2 me-1" onclick="openSessionForMarking(${s.id})">
                    <i class="fas fa-edit"></i> Sheet
                </button>
                <a class="btn btn-sm btn-erp-outline py-1 px-2" href="/biometric/live.html?sessionId=${s.id}">
                    <i class="fas fa-video text-danger"></i> Live
                </a>
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function openSessionForMarking(sessionId) {
    currentMarkingSessionId = sessionId;
    try {
        const sessionRes = await SmartAttendAPI.get(`/attendance/sessions/${sessionId}`);
        const recordsRes = await SmartAttendAPI.get(`/attendance/sessions/${sessionId}/records`);

        const session = sessionRes.data;
        currentSessionRecords = recordsRes.data || [];

        // Update Marking Sheet Header
        document.getElementById('marking-session-title').textContent = `${session.courseCode} — ${session.subjectName}`;
        document.getElementById('marking-session-meta').textContent = `${session.sessionCode} | Section: ${session.sectionName} | Period: ${session.periodNumber} | Date: ${session.sessionDate}`;

        const lockBtn = document.getElementById('btn-lock-session');
        if (lockBtn) {
            if (session.isLocked) {
                lockBtn.disabled = true;
                lockBtn.innerHTML = '<i class="fas fa-lock me-1"></i> Session Locked';
                lockBtn.className = 'btn btn-secondary';
            } else {
                lockBtn.disabled = false;
                lockBtn.innerHTML = '<i class="fas fa-lock-open me-1"></i> Lock Attendance Session';
                lockBtn.className = 'btn btn-erp-primary';
            }
        }

        renderMarkingRecords(currentSessionRecords, session.isLocked);
    } catch (err) {
        console.error('Failed to load session marking sheet:', err);
    }
}

function renderMarkingRecords(records, isLocked) {
    const tbody = document.getElementById('marking-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';
    if (records.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-muted">No student enrollment records in this section.</td></tr>';
        return;
    }

    records.forEach((r, idx) => {
        const row = document.createElement('tr');
        const st = r.status || 'ABSENT';

        row.innerHTML = `
            <td>${idx + 1}</td>
            <td><strong>${r.rollNo}</strong></td>
            <td>${r.studentName}</td>
            <td><span class="badge ${r.biometricEventId ? 'bg-success-subtle text-success' : 'bg-secondary-subtle text-muted'}">${r.biometricEventId ? '✓ Biometric Verified' : 'Manual / Pending'}</span></td>
            <td>
                <div class="btn-group btn-group-sm" role="group">
                    <button type="button" class="btn ${st === 'PRESENT' ? 'btn-success text-white' : 'btn-outline-success'}" ${isLocked ? 'disabled' : ''} onclick="updateRecordStatus(${r.id}, 'PRESENT')">P</button>
                    <button type="button" class="btn ${st === 'ABSENT' ? 'btn-danger text-white' : 'btn-outline-danger'}" ${isLocked ? 'disabled' : ''} onclick="updateRecordStatus(${r.id}, 'ABSENT')">A</button>
                    <button type="button" class="btn ${st === 'LATE' ? 'btn-warning text-dark' : 'btn-outline-warning'}" ${isLocked ? 'disabled' : ''} onclick="updateRecordStatus(${r.id}, 'LATE')">L</button>
                    <button type="button" class="btn ${st === 'EXCUSED' ? 'btn-info text-white' : 'btn-outline-info'}" ${isLocked ? 'disabled' : ''} onclick="updateRecordStatus(${r.id}, 'EXCUSED')">E</button>
                </div>
            </td>
            <td>
                <input type="text" class="form-control form-control-sm" placeholder="Remarks" value="${r.remarks || ''}" ${isLocked ? 'disabled' : ''} onchange="updateRecordRemarks(${r.id}, this.value)">
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function updateRecordStatus(recordId, status) {
    try {
        await SmartAttendAPI.put(`/attendance/records/${recordId}`, {
            status: status,
            remarks: 'Updated by faculty in manual sheet'
        });
        // Refresh session sheet
        openSessionForMarking(currentMarkingSessionId);
    } catch (e) {
        SmartAttendAPI.showToast(e.message || 'Failed to update record', 'danger');
    }
}

async function updateRecordRemarks(recordId, remarks) {
    const record = currentSessionRecords.find(r => r.id === recordId);
    if (!record) return;

    try {
        await SmartAttendAPI.put(`/attendance/records/${recordId}`, {
            status: record.status,
            remarks: remarks
        });
        SmartAttendAPI.showToast('Remark saved', 'info');
    } catch (e) {
        SmartAttendAPI.showToast('Error saving remark', 'danger');
    }
}

async function markAll(status) {
    if (!currentMarkingSessionId) return;

    const bulkItems = currentSessionRecords.map(r => ({
        studentId: r.studentId,
        status: status,
        method: 'MANUAL',
        remarks: `Bulk marked as ${status}`
    }));

    try {
        await SmartAttendAPI.post('/attendance/records/bulk', {
            sessionId: currentMarkingSessionId,
            records: bulkItems
        });
        SmartAttendAPI.showToast(`All students marked as ${status}`, 'success');
        openSessionForMarking(currentMarkingSessionId);
    } catch (e) {
        SmartAttendAPI.showToast(e.message || 'Bulk mark failed', 'danger');
    }
}

async function createNewSession(e) {
    e.preventDefault();
    const subjectId = document.getElementById('modal-subject-select').value;
    const sectionId = document.getElementById('modal-section-select').value;
    const periodNumber = document.getElementById('modal-period-select').value;
    const sessionDate = document.getElementById('modal-date-input').value || new Date().toISOString().split('T')[0];
    const mode = document.getElementById('modal-mode-select').value;

    try {
        const response = await SmartAttendAPI.post('/attendance/sessions', {
            subjectId: parseInt(subjectId),
            sectionId: parseInt(sectionId),
            periodNumber: parseInt(periodNumber),
            sessionDate: sessionDate,
            startTime: '10:00:00',
            endTime: '11:00:00',
            roomNo: 'Room 201',
            verificationMode: mode,
            notes: 'Session created from Faculty portal'
        });

        if (response.success) {
            SmartAttendAPI.showToast('Attendance Session Created!', 'success');
            const modalEl = document.getElementById('createSessionModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            if (modal) modal.hide();

            await loadActiveSessions();
            openSessionForMarking(response.data.id);
        }
    } catch (err) {
        SmartAttendAPI.showToast(err.message || 'Session creation failed', 'danger');
    }
}

async function lockCurrentSession() {
    if (!currentMarkingSessionId) return;

    if (!confirm('Are you sure you want to LOCK this attendance session? Once locked, attendance summaries and BEU marks are automatically computed and further manual edits are disabled.')) {
        return;
    }

    try {
        const response = await SmartAttendAPI.post(`/attendance/sessions/${currentMarkingSessionId}/lock`, {});
        if (response.success) {
            SmartAttendAPI.showToast('Attendance Session Locked & Summaries Computed!', 'success');
            await loadActiveSessions();
            openSessionForMarking(currentMarkingSessionId);
        }
    } catch (e) {
        SmartAttendAPI.showToast(e.message || 'Failed to lock session', 'danger');
    }
}

window.openSessionForMarking = openSessionForMarking;
window.updateRecordStatus = updateRecordStatus;
window.updateRecordRemarks = updateRecordRemarks;
window.markAll = markAll;
window.createNewSession = createNewSession;
window.lockCurrentSession = lockCurrentSession;
