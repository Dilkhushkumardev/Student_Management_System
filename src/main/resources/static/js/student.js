/**
 * SmartAttend - Student Portal & Daily Attendance Tracker Controller
 */

let currentStudentId = 1;
let currentStudentRoll = '26CSE14';
let officialReportData = null;
let trackerSubjects = [];
let actionHistory = [];
let targetCriteria = 75;

document.addEventListener('DOMContentLoaded', async () => {
    SmartAttendAPI.initTheme();

    const user = SmartAttendAPI.getUser();
    if (!user) {
        window.location.href = '/login.html';
        return;
    }

    if (user.studentId) {
        currentStudentId = user.studentId;
    }
    if (user.rollNo) {
        currentStudentRoll = user.rollNo;
    }

    // Populate user profile in navigation/sidebar
    const nameEls = document.querySelectorAll('.student-name-display');
    nameEls.forEach(el => el.textContent = user.fullName || user.name || 'Dilkhush Kumar');

    const trackerNameEl = document.getElementById('tracker-student-name');
    if (trackerNameEl) trackerNameEl.textContent = user.fullName || 'Dilkhush Kumar';

    const trackerRegEl = document.getElementById('tracker-reg-no');
    if (trackerRegEl) trackerRegEl.textContent = user.registrationNo || '26105157014';

    // Load saved settings
    const savedTarget = localStorage.getItem('smartattend_target_criteria');
    if (savedTarget) {
        targetCriteria = parseFloat(savedTarget) || 75;
        const targetInput = document.getElementById('setting-target-pct');
        if (targetInput) targetInput.value = targetCriteria;
    }

    await loadStudentData();
});

// 1. Fetch Official Report from Backend & Init Tracker
async function loadStudentData() {
    try {
        const reportRes = await SmartAttendAPI.get(`/reports/student/${currentStudentId}`);
        if (reportRes && (reportRes.data || reportRes.success)) {
            officialReportData = reportRes.data || reportRes;
            initTrackerData(officialReportData);
            renderStudentDashboard(officialReportData);
            renderTrackerCards();
            renderDailySchedule();
        } else {
            throw new Error(reportRes.message || 'No report data received');
        }
    } catch (err) {
        console.warn('Backend report sync warning:', err);
        // If offline or first load, fallback to local storage
        initTrackerData(null);
        renderTrackerCards();
        renderDailySchedule();
    }
}

// Official 10 Subjects from BEU B.Tech 1st Semester Group-A (2026-2030) Syllabus
const OFFICIAL_SYLLABUS_SUBJECTS = [
    { id: 'sub_1', code: '100102', name: 'Engineering Mathematics - I', type: 'THEORY', credits: 3.0, present: 18, absent: 2, total: 20, isCustom: false },
    { id: 'sub_2', code: '100104', name: 'Engineering Physics', type: 'THEORY', credits: 3.0, present: 17, absent: 3, total: 20, isCustom: false },
    { id: 'sub_3', code: '100105', name: 'Introduction to AI', type: 'THEORY', credits: 3.0, present: 19, absent: 1, total: 20, isCustom: false },
    { id: 'sub_4', code: '100108', name: 'Computer Fundamentals & Emerging Technologies', type: 'THEORY', credits: 3.0, present: 18, absent: 2, total: 20, isCustom: false },
    { id: 'sub_5', code: '100109', name: 'Universal Human Values', type: 'THEORY', credits: 2.0, present: 16, absent: 2, total: 18, isCustom: false },
    { id: 'sub_6', code: '100110', name: 'Essence of Indian Constitution', type: 'THEORY', credits: 0.0, present: 16, absent: 2, total: 18, isCustom: false },
    { id: 'sub_7', code: '100111', name: 'Basics of Electrical & Electronics Engineering', type: 'THEORY', credits: 3.0, present: 17, absent: 3, total: 20, isCustom: false },
    { id: 'sub_8', code: '100104P', name: 'Engineering Physics Lab', type: 'PRACTICAL', credits: 1.0, present: 16, absent: 2, total: 18, isCustom: false },
    { id: 'sub_9', code: '100111P', name: 'Basics of Electrical & Electronics Engineering Lab', type: 'PRACTICAL', credits: 1.0, present: 16, absent: 2, total: 18, isCustom: false },
    { id: 'sub_10', code: '100112P', name: 'Programming for Problem Solving Lab', type: 'PRACTICAL', credits: 1.0, present: 18, absent: 0, total: 18, isCustom: false }
];

// 2. Initialize Tracker Subjects (with persistent localStorage support)
function initTrackerData(report) {
    const storageKey = `smartattend_tracker_${currentStudentRoll}`;
    const saved = localStorage.getItem(storageKey);

    // List of dummy / demo subjects to purge
    const dummyNames = [
        'computer network', 'compiler design', 'machine learning', 
        'graph theory', 'internet and web technology', 'computer network lab', 
        'compiler design lab', 'python lab', 'biometric', '⚡ biometric ☠️💥'
    ];

    const isDummy = (name) => {
        if (!name) return false;
        const lower = name.toLowerCase().trim();
        return dummyNames.some(d => lower.includes(d));
    };

    if (saved) {
        try {
            const parsed = JSON.parse(saved);
            if (Array.isArray(parsed)) {
                // Filter out any dummy subjects from previously saved cache
                const cleaned = parsed.filter(s => !isDummy(s.name));
                if (cleaned.length > 0) {
                    trackerSubjects = cleaned;
                    saveTrackerData();
                    return;
                }
            }
        } catch (e) {
            console.error('Error reading local tracker:', e);
        }
    }

    // Populate from official BEU database report
    const initialList = [];

    if (report && report.subjectRows && report.subjectRows.length > 0) {
        report.subjectRows.forEach((s, idx) => {
            if (!isDummy(s.subjectName)) {
                initialList.push({
                    id: `beu_${s.subjectId || idx}`,
                    code: s.courseCode || `SUB${idx+1}`,
                    name: s.subjectName || 'Subject',
                    type: s.subjectType || 'THEORY',
                    present: s.presentClasses || 0,
                    absent: s.absentClasses || 0,
                    total: s.totalClasses || 0,
                    isCustom: false
                });
            }
        });
    }

    // If report was empty or missing, use the official 10 syllabus subjects
    if (initialList.length === 0) {
        OFFICIAL_SYLLABUS_SUBJECTS.forEach(s => {
            initialList.push({ ...s });
        });
    }

    trackerSubjects = initialList;
    saveTrackerData();
}

function saveTrackerData() {
    const storageKey = `smartattend_tracker_${currentStudentRoll}`;
    localStorage.setItem(storageKey, JSON.stringify(trackerSubjects));
    updateTrackerOverallStats();
}

// 3. Render Attendance Tracker Cards (Matching User's Screenshot!)
function renderTrackerCards() {
    const container = document.getElementById('tracker-cards-list');
    if (!container) return;

    container.innerHTML = '';

    const countBadge = document.getElementById('tracker-subject-count');
    if (countBadge) countBadge.textContent = `${trackerSubjects.length} Subjects`;

    if (trackerSubjects.length === 0) {
        container.innerHTML = `
            <div class="text-center py-5 text-muted">
                <i class="fas fa-folder-open fa-3x mb-3 opacity-50"></i>
                <p>No subjects in your tracker yet. Click the <strong>+</strong> button below to add your courses!</p>
            </div>
        `;
        return;
    }

    trackerSubjects.forEach(sub => {
        const card = createTrackerItemElement(sub);
        container.appendChild(card);
    });
}

function createTrackerItemElement(sub) {
    const div = document.createElement('div');
    div.className = 'tracker-item-card';

    const total = sub.total || 0;
    const present = sub.present || 0;
    const absent = sub.absent || (total - present);
    const pct = total > 0 ? (present * 100.0 / total) : 0;
    const pctFormatted = pct.toFixed(0);

    // Color Theme: Green (>=75%), Yellow (60-74.9%), Pink (<60%)
    let colorClass = 'green';
    if (pct < 60) colorClass = 'pink';
    else if (pct < targetCriteria) colorClass = 'yellow';

    // SVG Gauge calculation (radius 22 -> circumference 138.23)
    const radius = 22;
    const circumference = 2 * Math.PI * radius;
    const offset = circumference - (pct / 100) * circumference;

    // Bunk & Shortage Advice Pill
    let adviceHtml = '';
    if (total === 0) {
        adviceHtml = `<span class="bunk-pill safe"><i class="fas fa-info-circle"></i> No classes recorded yet</span>`;
    } else if (pct < targetCriteria) {
        const needed = Math.ceil(( (targetCriteria / 100.0) * total - present ) / ( 1 - (targetCriteria / 100.0) ));
        adviceHtml = `<span class="bunk-pill danger"><i class="fas fa-triangle-exclamation"></i> Attend next ${Math.max(1, needed)} class(es) for ${targetCriteria}%</span>`;
    } else {
        const canBunk = Math.floor((present / (targetCriteria / 100.0)) - total);
        if (canBunk > 0) {
            adviceHtml = `<span class="bunk-pill safe"><i class="fas fa-shield-halved"></i> Safe to miss ${canBunk} class(es)</span>`;
        } else {
            adviceHtml = `<span class="bunk-pill warning"><i class="fas fa-circle-check"></i> On track (${targetCriteria}% threshold)</span>`;
        }
    }

    div.innerHTML = `
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center gap-3 flex-grow-1 me-3">
                <div class="tracker-dot"></div>
                <div>
                    <div class="tracker-subject-title">${escapeHtml(sub.name)}</div>
                    <div class="text-muted small mt-1">
                        <span class="fw-semibold text-success">${present} Present</span> &bull; 
                        <span class="text-danger">${absent} Absent</span> &bull; 
                        <span>${total} Total</span>
                    </div>
                </div>
            </div>

            <div class="circle-gauge-box">
                <svg class="circle-gauge-svg" viewBox="0 0 52 52">
                    <circle class="circle-bg" cx="26" cy="26" r="${radius}"></circle>
                    <circle class="circle-progress ${colorClass}" cx="26" cy="26" r="${radius}"
                            stroke-dasharray="${circumference}"
                            stroke-dashoffset="${offset}"></circle>
                </svg>
                <span class="circle-percentage-text ${colorClass}">${pctFormatted}%</span>
            </div>
        </div>

        <div class="mt-2">
            ${adviceHtml}
        </div>

        <!-- 1-Tap Attendance Actions -->
        <div class="tracker-actions-bar">
            <button class="btn-track-action btn-track-present" onclick="markAttendance('${sub.id}', 'PRESENT')" title="Mark Present (+1)">
                <i class="fas fa-check"></i> Present
            </button>
            <button class="btn-track-action btn-track-absent" onclick="markAttendance('${sub.id}', 'ABSENT')" title="Mark Absent (+1)">
                <i class="fas fa-xmark"></i> Absent
            </button>
            <button class="btn-track-action btn-track-undo" onclick="undoAttendance('${sub.id}')" title="Undo last action">
                <i class="fas fa-rotate-left"></i>
            </button>
            ${sub.isCustom ? `
                <button class="btn-track-action btn-track-undo text-danger" onclick="deleteCustomSubject('${sub.id}')" title="Delete Subject">
                    <i class="fas fa-trash-can"></i>
                </button>
            ` : ''}
        </div>
    `;

    return div;
}

// 4. Interactive Attendance Counter Logic (+Present / -Absent)
function markAttendance(subjectId, status) {
    const sub = trackerSubjects.find(s => s.id === subjectId);
    if (!sub) return;

    // Push action to undo stack
    actionHistory.push({
        subjectId: sub.id,
        prevPresent: sub.present,
        prevAbsent: sub.absent,
        prevTotal: sub.total,
        status
    });

    if (status === 'PRESENT') {
        sub.present = (sub.present || 0) + 1;
        sub.total = (sub.total || 0) + 1;
        SmartAttendAPI.showToast(`Marked PRESENT in ${sub.name}`, 'success');
    } else if (status === 'ABSENT') {
        sub.absent = (sub.absent || 0) + 1;
        sub.total = (sub.total || 0) + 1;
        SmartAttendAPI.showToast(`Marked ABSENT in ${sub.name}`, 'danger');
    }

    saveTrackerData();
    renderTrackerCards();
    renderDailySchedule();
}

function undoAttendance(subjectId) {
    const lastActionIdx = actionHistory.map(a => a.subjectId).lastIndexOf(subjectId);
    if (lastActionIdx === -1) {
        SmartAttendAPI.showToast('No recent actions to undo for this subject', 'info');
        return;
    }

    const lastAction = actionHistory.splice(lastActionIdx, 1)[0];
    const sub = trackerSubjects.find(s => s.id === lastAction.subjectId);
    if (sub) {
        sub.present = lastAction.prevPresent;
        sub.absent = lastAction.prevAbsent;
        sub.total = lastAction.prevTotal;
        saveTrackerData();
        renderTrackerCards();
        renderDailySchedule();
        SmartAttendAPI.showToast(`Undone last action for ${sub.name}`, 'info');
    }
}

function deleteCustomSubject(subjectId) {
    if (!confirm('Are you sure you want to remove this subject from your tracker?')) return;
    trackerSubjects = trackerSubjects.filter(s => s.id !== subjectId);
    saveTrackerData();
    renderTrackerCards();
    renderDailySchedule();
    SmartAttendAPI.showToast('Subject removed from tracker', 'info');
}

// 5. Update Overall KPI Stats across all subjects
function updateTrackerOverallStats() {
    let totalClasses = 0;
    let totalPresent = 0;
    let shortageCount = 0;

    trackerSubjects.forEach(s => {
        totalClasses += (s.total || 0);
        totalPresent += (s.present || 0);
        const p = s.total > 0 ? (s.present * 100.0 / s.total) : 0;
        if (p < targetCriteria) shortageCount++;
    });

    const overallPct = totalClasses > 0 ? (totalPresent * 100.0 / totalClasses).toFixed(2) : '0.00';

    const trackerPctEl = document.getElementById('tracker-overall-pct');
    if (trackerPctEl) trackerPctEl.textContent = `${overallPct}%`;

    const overallPctEl = document.getElementById('overall-pct');
    if (overallPctEl) overallPctEl.textContent = `${overallPct}%`;

    const overallTotalEl = document.getElementById('overall-total');
    if (overallTotalEl) overallTotalEl.textContent = totalClasses;

    const overallPresentEl = document.getElementById('overall-present');
    if (overallPresentEl) overallPresentEl.textContent = totalPresent;

    const overallShortageEl = document.getElementById('overall-shortage-count');
    if (overallShortageEl) overallShortageEl.textContent = shortageCount;
}

// 6. Sub-View 2: Today Daily Timetable Logger
function renderDailySchedule() {
    const list = document.getElementById('daily-schedule-list');
    if (!list) return;

    list.innerHTML = '';

    if (trackerSubjects.length === 0) {
        list.innerHTML = '<div class="text-muted text-center py-3">No subjects available to schedule.</div>';
        return;
    }

    // Schedule 4 to 5 periods for today from the student's subjects
    const todayPeriods = [
        { period: 1, time: '10:00 - 11:00 AM', subject: trackerSubjects[0] || trackerSubjects[0] },
        { period: 2, time: '11:00 - 12:00 PM', subject: trackerSubjects[1] || trackerSubjects[0] },
        { period: 3, time: '12:00 - 01:00 PM', subject: trackerSubjects[2] || trackerSubjects[0] },
        { period: 4, time: '02:00 - 03:00 PM', subject: trackerSubjects[3] || trackerSubjects[0] },
        { period: 5, time: '03:00 - 04:00 PM', subject: trackerSubjects[4] || trackerSubjects[0] }
    ];

    todayPeriods.forEach(p => {
        if (!p.subject) return;
        const row = document.createElement('div');
        row.className = 'd-flex justify-content-between align-items-center p-3 mb-2 rounded-3 border bg-card';

        row.innerHTML = `
            <div>
                <span class="badge bg-primary-subtle text-primary mb-1">Period ${p.period} (${p.time})</span>
                <h6 class="fw-bold mb-0">${escapeHtml(p.subject.name)}</h6>
                <small class="text-muted">Current Attendance: ${p.subject.present}/${p.subject.total}</small>
            </div>
            <div class="d-flex gap-2">
                <button class="btn btn-sm btn-outline-success" onclick="markAttendance('${p.subject.id}', 'PRESENT')">
                    <i class="fas fa-check"></i> Present
                </button>
                <button class="btn btn-sm btn-outline-danger" onclick="markAttendance('${p.subject.id}', 'ABSENT')">
                    <i class="fas fa-xmark"></i> Absent
                </button>
            </div>
        `;
        list.appendChild(row);
    });
}

// 7. View & Sub-Tab Navigation
function switchMainView(viewMode) {
    const trackerView = document.getElementById('view-tracker');
    const officialView = document.getElementById('view-official');
    const btnTracker = document.getElementById('tab-btn-tracker');
    const btnOfficial = document.getElementById('tab-btn-official');

    if (viewMode === 'tracker') {
        if (trackerView) trackerView.classList.remove('d-none');
        if (officialView) officialView.classList.add('d-none');
        if (btnTracker) {
            btnTracker.className = 'btn btn-sm rounded-pill px-3 fw-semibold active';
        }
        if (btnOfficial) {
            btnOfficial.className = 'btn btn-sm rounded-pill px-3 fw-semibold text-muted';
        }
    } else {
        if (trackerView) trackerView.classList.add('d-none');
        if (officialView) officialView.classList.remove('d-none');
        if (btnTracker) {
            btnTracker.className = 'btn btn-sm rounded-pill px-3 fw-semibold text-muted';
        }
        if (btnOfficial) {
            btnOfficial.className = 'btn btn-sm rounded-pill px-3 fw-semibold active';
        }
    }
}

function switchTrackerSubTab(tabName) {
    const tabs = ['home', 'today', 'analytics', 'settings'];
    tabs.forEach(t => {
        const pane = document.getElementById(`subtab-${t}`);
        const navBtn = document.getElementById(`bnav-${t}`);
        if (t === tabName) {
            if (pane) pane.classList.remove('d-none');
            if (navBtn) navBtn.classList.add('active');
        } else {
            if (pane) pane.classList.add('d-none');
            if (navBtn) navBtn.classList.remove('active');
        }
    });

    if (tabName === 'today') renderDailySchedule();
    if (tabName === 'analytics') updateTrackerOverallStats();
}

// 8. Custom Subject Modal Handler
function openAddSubjectModal() {
    const modalEl = document.getElementById('addSubjectModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

function handleSaveCustomSubject(e) {
    e.preventDefault();
    const nameInput = document.getElementById('custom-sub-name');
    const presentInput = document.getElementById('custom-sub-present');
    const totalInput = document.getElementById('custom-sub-total');
    const typeSelect = document.getElementById('custom-sub-type');

    const name = nameInput.value.trim();
    const present = parseInt(presentInput.value, 10) || 0;
    const total = parseInt(totalInput.value, 10) || 0;
    const type = typeSelect.value || 'THEORY';

    if (!name) return;

    const newSub = {
        id: 'cust_' + Date.now(),
        code: 'CUSTOM',
        name: name,
        type: type,
        present: present,
        absent: Math.max(0, total - present),
        total: Math.max(present, total),
        isCustom: true
    };

    trackerSubjects.push(newSub);
    saveTrackerData();
    renderTrackerCards();
    renderDailySchedule();

    const modalEl = document.getElementById('addSubjectModal');
    const modal = bootstrap.Modal.getInstance(modalEl);
    if (modal) modal.hide();

    nameInput.value = '';
    presentInput.value = '0';
    totalInput.value = '0';

    SmartAttendAPI.showToast(`Added ${name} to Attendance Tracker`, 'success');
}

function updateTargetPercentage(val) {
    const num = parseFloat(val);
    if (num >= 50 && num <= 100) {
        targetCriteria = num;
        localStorage.setItem('smartattend_target_criteria', num);
        renderTrackerCards();
        SmartAttendAPI.showToast(`Target attendance threshold updated to ${num}%`, 'success');
    }
}

function resetToOfficialReport() {
    const storageKey = `smartattend_tracker_${currentStudentRoll}`;
    localStorage.removeItem(storageKey);
    initTrackerData(officialReportData);
    renderTrackerCards();
    renderDailySchedule();
    SmartAttendAPI.showToast('Synced with BEU University Attendance Records', 'success');
}

function clearAllTrackerData() {
    if (!confirm('Are you sure you want to reset all tracker data?')) return;
    const storageKey = `smartattend_tracker_${currentStudentRoll}`;
    localStorage.removeItem(storageKey);
    trackerSubjects = [];
    saveTrackerData();
    renderTrackerCards();
    renderDailySchedule();
    SmartAttendAPI.showToast('Tracker data cleared', 'info');
}

// 9. Official BEU Format Table Renderer
function renderStudentDashboard(report) {
    if (!report) return;

    const profName = document.getElementById('profile-name');
    if (profName) profName.textContent = report.studentName || 'Dilkhush Kumar';

    const profRoll = document.getElementById('profile-roll');
    if (profRoll) profRoll.textContent = report.rollNumber || report.rollNo || '26CSE14';

    const profReg = document.getElementById('profile-reg');
    if (profReg) profReg.textContent = report.registrationNumber || report.universityRegNo || '26105157014';

    const profDept = document.getElementById('profile-dept');
    if (profDept) profDept.textContent = report.department || report.departmentName || 'Computer Science & Engineering';

    const profSec = document.getElementById('profile-sec');
    if (profSec) profSec.textContent = `${report.section || report.sectionName || 'Section A'} (${report.semester || report.semesterName || '1st Semester'})`;

    const profBio = document.getElementById('profile-bio');
    if (profBio) profBio.textContent = report.biometricId || '26105157014';

    const subjects = report.subjectRows || report.subjectSummaries || [];
    renderReportTable(report, subjects);
}

function renderReportTable(report, subjectsList) {
    const tbody = document.getElementById('report-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';
    const subjects = subjectsList || report.subjectRows || [];

    if (subjects.length === 0) {
        tbody.innerHTML = '<tr><td colspan="10" class="text-center py-3 text-muted">No enrolled subject attendance records found.</td></tr>';
        return;
    }

    subjects.forEach((s, idx) => {
        const row = document.createElement('tr');
        const pct = parseFloat(s.percentage !== undefined ? s.percentage : 0).toFixed(2);
        const status = s.status || (pct >= 75 ? 'ELIGIBLE' : (pct >= 60 ? 'CONDONABLE_MEDICAL' : 'SHORTAGE'));
        const marks = s.attendanceMarks !== undefined ? s.attendanceMarks : 0;

        row.innerHTML = `
            <td>${idx + 1}</td>
            <td><strong>${escapeHtml(s.courseCode || '')}</strong></td>
            <td>${escapeHtml(s.subjectName || '')}</td>
            <td><span class="badge ${s.subjectType === 'PRACTICAL' ? 'bg-info-subtle text-info' : 'bg-primary-subtle text-primary'}">${s.subjectType || 'THEORY'}</span></td>
            <td class="text-center">${s.totalClasses || 0}</td>
            <td class="text-center text-success fw-bold">${s.presentClasses || 0}</td>
            <td class="text-center text-danger">${s.absentClasses || 0}</td>
            <td class="text-center"><strong>${pct}%</strong></td>
            <td class="text-center fw-bold text-primary">${marks}/5</td>
            <td><span class="badge ${pct >= 75 ? 'bg-success' : (pct >= 60 ? 'bg-warning text-dark' : 'bg-danger')}">${status}</span></td>
        `;
        tbody.appendChild(row);
    });
}

function escapeHtml(text) {
    if (!text) return '';
    return String(text)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

async function exportCsv() {
    try {
        const blob = await SmartAttendAPI.get(`/reports/student/${currentStudentId}/export-csv`);
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `BEU_Attendance_Report_${currentStudentRoll}.csv`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        SmartAttendAPI.showToast('Attendance report CSV downloaded', 'success');
    } catch (e) {
        SmartAttendAPI.showToast('CSV export failed', 'danger');
    }
}

function printReport() {
    window.print();
}

// Global window bindings
window.switchMainView = switchMainView;
window.switchTrackerSubTab = switchTrackerSubTab;
window.markAttendance = markAttendance;
window.undoAttendance = undoAttendance;
window.deleteCustomSubject = deleteCustomSubject;
window.openAddSubjectModal = openAddSubjectModal;
window.handleSaveCustomSubject = handleSaveCustomSubject;
window.updateTargetPercentage = updateTargetPercentage;
window.resetToOfficialReport = resetToOfficialReport;
window.clearAllTrackerData = clearAllTrackerData;
window.exportCsv = exportCsv;
window.printReport = printReport;
window.renderDailySchedule = renderDailySchedule;
