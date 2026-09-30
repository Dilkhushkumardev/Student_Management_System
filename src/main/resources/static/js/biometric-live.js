/**
 * SmartAttend - Live Biometric Attendance Screen & Real-time SSE Handler
 */

let currentSessionId = 1;
let eventSource = null;

document.addEventListener('DOMContentLoaded', async () => {
    SmartAttendAPI.initTheme();

    // Fetch active session or populate selector
    await loadActiveSessions();
    await loadStudentsDropdown();

    // Event listeners
    const sessionSelect = document.getElementById('session-select');
    if (sessionSelect) {
        sessionSelect.addEventListener('change', (e) => {
            currentSessionId = parseInt(e.target.value);
            initSessionView(currentSessionId);
        });
    }

    // Connect SSE
    initSessionView(currentSessionId);
});

async function loadActiveSessions() {
    try {
        const response = await SmartAttendAPI.get('/attendance/sessions');
        const sessionSelect = document.getElementById('session-select');
        if (!sessionSelect) return;

        sessionSelect.innerHTML = '';
        const sessions = (response.data && response.data.content) ? response.data.content : (response.data || []);

        if (sessions.length === 0) {
            sessionSelect.innerHTML = '<option value="1">Default Demo Session (Mathematics - I)</option>';
            currentSessionId = 1;
        } else {
            sessions.forEach(s => {
                const opt = document.createElement('option');
                opt.value = s.id;
                opt.textContent = `${s.courseCode || 'SUB'} - ${s.subjectName || 'Subject'} (${s.sectionName || 'Sec A'}) [${s.status}]`;
                sessionSelect.appendChild(opt);
            });
            currentSessionId = sessions[0].id;
        }
    } catch (err) {
        console.warn('Could not load sessions list, fallback to session 1:', err);
        currentSessionId = 1;
    }
}

async function loadStudentsDropdown() {
    try {
        const response = await SmartAttendAPI.get('/students?size=50');
        const studentSelect = document.getElementById('student-select');
        if (!studentSelect) return;

        studentSelect.innerHTML = '<option value="">-- Choose Student to Simulate --</option>';
        const students = (response.data && response.data.content) ? response.data.content : (response.data || []);

        students.forEach(s => {
            const opt = document.createElement('option');
            opt.value = s.id;
            opt.dataset.roll = s.rollNo;
            opt.dataset.name = s.name;
            opt.textContent = `${s.rollNo} - ${s.name} (${s.biometricId || 'BIO-DEFAULT'})`;
            studentSelect.appendChild(opt);
        });
    } catch (err) {
        console.warn('Could not load student list:', err);
    }
}

async function initSessionView(sessionId) {
    if (!sessionId) return;

    // 1. Fetch initial counter data
    try {
        const res = await SmartAttendAPI.get(`/biometric/live-counter/${sessionId}`);
        if (res.success && res.data) {
            updateCounterUI(res.data);
        }
    } catch (err) {
        console.warn('Error fetching initial counter:', err);
    }

    // 2. Setup Server-Sent Events (SSE)
    if (eventSource) {
        eventSource.close();
    }

    try {
        eventSource = new EventSource(`/api/biometric/stream/${sessionId}`);

        eventSource.addEventListener('counter_update', (e) => {
            const counterData = JSON.parse(e.data);
            updateCounterUI(counterData);
        });

        eventSource.addEventListener('student_scan', (e) => {
            const scanData = JSON.parse(e.data);
            appendStudentFeed(scanData);
        });

        eventSource.onerror = (err) => {
            console.log('SSE connection status check (polling fallback active)');
        };
    } catch (e) {
        console.warn('SSE not supported or failed to initialize, using REST refresh');
    }
}

function updateCounterUI(data) {
    if (!data) return;

    // Update Session Details Banner
    const subEl = document.getElementById('session-subject');
    const secEl = document.getElementById('session-section');
    const facEl = document.getElementById('session-faculty');
    const codeEl = document.getElementById('session-code');

    if (subEl) subEl.textContent = data.subjectName || 'Engineering Mathematics - I';
    if (secEl) secEl.textContent = data.sectionName || 'Section A';
    if (facEl) facEl.textContent = data.facultyName || 'Dr. Rajesh Kumar';
    if (codeEl) codeEl.textContent = data.sessionCode || 'SESS-LIVE-001';

    // Update Counter Numbers
    document.getElementById('count-total').textContent = data.totalStudents || 0;
    document.getElementById('count-present').textContent = data.presentCount || 0;
    document.getElementById('count-absent').textContent = data.absentCount || 0;
    document.getElementById('count-late').textContent = data.lateCount || 0;
    document.getElementById('count-not-verified').textContent = data.notVerifiedCount || 0;

    const pct = parseFloat(data.attendancePercentage || 0).toFixed(2);
    const pctEl = document.getElementById('percentage-display');
    const barEl = document.getElementById('percentage-bar');

    if (pctEl) pctEl.textContent = `${pct}%`;
    if (barEl) {
        barEl.style.width = `${pct}%`;
        if (pct >= 75) {
            barEl.className = 'progress-bar bg-success';
        } else if (pct >= 60) {
            barEl.className = 'progress-bar bg-warning';
        } else {
            barEl.className = 'progress-bar bg-danger';
        }
    }

    // Populate initial feed if provided
    if (data.recentFeed && data.recentFeed.length > 0) {
        const feedList = document.getElementById('live-feed-list');
        if (feedList && feedList.children.length <= 1) {
            feedList.innerHTML = '';
            data.recentFeed.forEach(item => appendStudentFeed(item, false));
        }
    }
}

function appendStudentFeed(item, prepend = true) {
    const feedList = document.getElementById('live-feed-list');
    if (!feedList) return;

    // Remove empty placeholder
    const emptyPlaceholder = document.getElementById('feed-empty-state');
    if (emptyPlaceholder) emptyPlaceholder.remove();

    const isDuplicate = item.verificationResult === 'DUPLICATE';
    const tagClass = isDuplicate ? 'tag-duplicate' : 'tag-verified';
    const tagText = isDuplicate ? 'DUPLICATE SCAN' : `✓ ${item.verificationType || 'VERIFIED'}`;

    const feedItem = document.createElement('div');
    feedItem.className = 'feed-item';
    feedItem.innerHTML = `
        <div class="d-flex align-items-center gap-3">
            <span class="time">${item.timeAgo || new Date().toLocaleTimeString()}</span>
            <span class="roll">${item.rollNo || 'STU'}</span>
            <span class="name">${item.studentName || 'Student'}</span>
        </div>
        <span class="tag ${tagClass}">${tagText}</span>
    `;

    if (prepend) {
        feedList.insertBefore(feedItem, feedList.firstChild);
    } else {
        feedList.appendChild(feedItem);
    }
}

// Demo Simulation Handlers
async function simulateSingleScan(verificationType) {
    const studentSelect = document.getElementById('student-select');
    const studentId = studentSelect ? studentSelect.value : null;

    if (!studentId) {
        SmartAttendAPI.showToast('Please select a student from the dropdown to simulate scan', 'warning');
        return;
    }

    try {
        const response = await SmartAttendAPI.post('/biometric/simulate', {
            sessionId: currentSessionId,
            studentId: parseInt(studentId),
            verificationType: verificationType,
            simulateAllRemaining: false
        });

        if (response.success) {
            updateCounterUI(response.data);
            SmartAttendAPI.showToast(`Biometric ${verificationType} scan processed!`, 'success');
        }
    } catch (err) {
        SmartAttendAPI.showToast(err.message || 'Simulation error', 'danger');
    }
}

async function simulateBulkClass() {
    const btn = document.getElementById('btn-simulate-bulk');
    if (btn) btn.disabled = true;

    try {
        const response = await SmartAttendAPI.post('/biometric/simulate', {
            sessionId: currentSessionId,
            verificationType: 'FINGERPRINT',
            simulateAllRemaining: true
        });

        if (response.success) {
            updateCounterUI(response.data);
            SmartAttendAPI.showToast('Entire section attendance simulated via biometric terminal!', 'success');
        }
    } catch (err) {
        SmartAttendAPI.showToast(err.message || 'Bulk simulation error', 'danger');
    } finally {
        if (btn) btn.disabled = false;
    }
}

window.simulateSingleScan = simulateSingleScan;
window.simulateBulkClass = simulateBulkClass;
