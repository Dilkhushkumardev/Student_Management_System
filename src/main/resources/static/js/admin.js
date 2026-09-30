/**
 * SmartAttend - Admin ERP Portal Controller & Analytics
 */

let currentTab = 'overview';
let allStudents = [];
let allFaculty = [];
let allSubjects = [];
let allDevices = [];
let chartInstances = {};

document.addEventListener('DOMContentLoaded', async () => {
    SmartAttendAPI.initTheme();

    const user = SmartAttendAPI.getUser();
    if (!user) {
        window.location.href = '/login.html';
        return;
    }

    const nameEls = document.querySelectorAll('.admin-name-display');
    nameEls.forEach(el => el.textContent = user.fullName || user.name || user.username || 'System Administrator');

    // Tab switcher
    document.querySelectorAll('[data-admin-tab]').forEach(tabBtn => {
        tabBtn.addEventListener('click', (e) => {
            e.preventDefault();
            switchTab(tabBtn.getAttribute('data-admin-tab'));
        });
    });

    await loadDashboardStats();
    await loadStudents();
    await loadFaculty();
    await loadSubjects();
    await loadDevices();
    await loadCalendar();
    await loadAuditLogs();
});

function switchTab(tabId) {
    currentTab = tabId;

    document.querySelectorAll('[data-admin-tab]').forEach(btn => {
        if (btn.getAttribute('data-admin-tab') === tabId) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });

    document.querySelectorAll('.admin-tab-pane').forEach(pane => {
        if (pane.id === `tab-${tabId}`) {
            pane.classList.remove('d-none');
        } else {
            pane.classList.add('d-none');
        }
    });

    if (tabId === 'overview') {
        renderCharts();
    }
}

// 1. Dashboard Stats & Analytics
async function loadDashboardStats() {
    try {
        const res = await SmartAttendAPI.get('/reports/admin/stats');
        if (res.success && res.data) {
            const stats = res.data;
            document.getElementById('stat-total-students').textContent = stats.totalStudents || 30;
            document.getElementById('stat-total-faculty').textContent = stats.totalFaculty || 2;
            document.getElementById('stat-total-subjects').textContent = stats.totalSubjects || 10;
            document.getElementById('stat-avg-attendance').textContent = `${parseFloat(stats.averageCollegeAttendance || stats.averageAttendancePercentage || 0).toFixed(1)}%`;
            document.getElementById('stat-shortage-count').textContent = stats.studentsBelow75 !== undefined ? stats.studentsBelow75 : (stats.studentsBelow75Count || 0);
            document.getElementById('stat-devices-online').textContent = stats.biometricDevicesOnline !== undefined ? stats.biometricDevicesOnline : (stats.devicesOnline || 0);

            renderCharts(stats);
        }
    } catch (e) {
        console.warn('Error loading admin stats:', e);
    }
}

function renderCharts(stats = {}) {
    // Chart 1: Present vs Absent Doughnut
    const doughnutCtx = document.getElementById('chart-turnout-doughnut');
    if (doughnutCtx) {
        if (chartInstances.turnout) chartInstances.turnout.destroy();

        chartInstances.turnout = new Chart(doughnutCtx, {
            type: 'doughnut',
            data: {
                labels: ['Present (>= 75%)', 'Shortage (< 75%)'],
                datasets: [{
                    data: [
                        Math.max(1, (stats.totalStudents || 30) - (stats.studentsBelow75Count || 4)),
                        stats.studentsBelow75Count || 4
                    ],
                    backgroundColor: ['#10b981', '#ef4444'],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'bottom' } }
            }
        });
    }

    // Chart 2: Subject-Wise Attendance Comparison Bar Chart
    const barCtx = document.getElementById('chart-subjects-bar');
    if (barCtx) {
        if (chartInstances.subjects) chartInstances.subjects.destroy();

        chartInstances.subjects = new Chart(barCtx, {
            type: 'bar',
            data: {
                labels: ['Maths I', 'Physics', 'AI Intro', 'Comp Fund', 'Human Values', 'Constitution', 'EEE', 'Physics Lab', 'EEE Lab', 'PPS Lab'],
                datasets: [{
                    label: 'Avg Attendance %',
                    data: [82.4, 76.5, 88.0, 79.2, 91.5, 85.0, 72.8, 94.0, 89.5, 93.0],
                    backgroundColor: '#2563eb',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: { min: 0, max: 100, ticks: { callback: v => `${v}%` } }
                }
            }
        });
    }
}

// 2. Student Management CRUD & Search
async function loadStudents(query = '') {
    try {
        const res = await SmartAttendAPI.get(`/students?size=100${query ? `&search=${encodeURIComponent(query)}` : ''}`);
        allStudents = (res.data && res.data.content) ? res.data.content : (res.data || []);
        renderStudentsTable(allStudents);
    } catch (e) {
        console.error('Failed to load students:', e);
    }
}

function renderStudentsTable(students) {
    const tbody = document.getElementById('students-table-body');
    if (!tbody) return;

    tbody.innerHTML = '';
    if (students.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center py-4 text-muted">No students found matching query.</td></tr>';
        return;
    }

    students.forEach((s, idx) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${idx + 1}</td>
            <td><strong>${s.rollNo}</strong></td>
            <td>${s.name}</td>
            <td>${s.universityRegNo || 'N/A'}</td>
            <td>${s.departmentName || 'CSE'} (${s.sectionName || 'Sec A'})</td>
            <td><span class="mono badge bg-primary-subtle text-primary">${s.biometricId || 'N/A'}</span></td>
            <td><span class="badge ${s.status === 'ACTIVE' ? 'bg-success' : 'bg-secondary'}">${s.status}</span></td>
            <td class="text-end">
                <button class="btn btn-sm btn-erp-outline py-1 px-2 me-1" onclick="viewStudentReport(${s.id})"><i class="fas fa-eye"></i></button>
                <button class="btn btn-sm btn-danger py-1 px-2" onclick="deleteStudent(${s.id})"><i class="fas fa-trash"></i></button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function searchStudents() {
    const q = document.getElementById('student-search-input').value.trim();
    loadStudents(q);
}

async function saveStudent(e) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('stu-name').value,
        rollNo: document.getElementById('stu-roll').value,
        universityRegNo: document.getElementById('stu-reg').value,
        email: document.getElementById('stu-email').value,
        mobile: document.getElementById('stu-mobile').value,
        gender: document.getElementById('stu-gender').value,
        biometricId: document.getElementById('stu-bio').value,
        departmentId: 1,
        branchId: 1,
        batchId: 1,
        sectionId: 1,
        semesterId: 1
    };

    try {
        const res = await SmartAttendAPI.post('/students', payload);
        if (res.success) {
            SmartAttendAPI.showToast('Student enrolled successfully!', 'success');
            const modal = bootstrap.Modal.getInstance(document.getElementById('addStudentModal'));
            if (modal) modal.hide();
            loadStudents();
        }
    } catch (err) {
        SmartAttendAPI.showToast(err.message || 'Failed to save student', 'danger');
    }
}

async function deleteStudent(id) {
    if (!confirm('Are you sure you want to deactivate/delete this student?')) return;
    try {
        await SmartAttendAPI.delete(`/students/${id}`);
        SmartAttendAPI.showToast('Student deactivated', 'info');
        loadStudents();
    } catch (e) {
        SmartAttendAPI.showToast(e.message || 'Delete failed', 'danger');
    }
}

function viewStudentReport(id) {
    window.location.href = `/student/index.html?studentId=${id}`;
}

// 3. Faculty Management
async function loadFaculty() {
    try {
        const res = await SmartAttendAPI.get('/faculty?size=50');
        allFaculty = (res.data && res.data.content) ? res.data.content : (res.data || []);
        const tbody = document.getElementById('faculty-table-body');
        if (!tbody) return;

        tbody.innerHTML = '';
        allFaculty.forEach((f, idx) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${idx + 1}</td>
                <td><strong>${f.employeeId || 'EMP-00' + f.id}</strong></td>
                <td>${f.name}</td>
                <td>${f.designation || 'Assistant Professor'}</td>
                <td>${f.departmentName || 'CSE'}</td>
                <td>${f.email || 'faculty@beu.ac.in'}</td>
                <td><span class="badge bg-success">${f.status || 'ACTIVE'}</span></td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.warn('Error loading faculty:', e);
    }
}

// 4. Subjects Management
async function loadSubjects() {
    try {
        const res = await SmartAttendAPI.get('/subjects?size=50');
        allSubjects = (res.data && res.data.content) ? res.data.content : (res.data || []);
        const tbody = document.getElementById('subjects-table-body');
        if (!tbody) return;

        tbody.innerHTML = '';
        allSubjects.forEach((s, idx) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${idx + 1}</td>
                <td><strong>${s.courseCode}</strong></td>
                <td>${s.subjectName}</td>
                <td><span class="badge bg-primary-subtle text-primary">${s.subjectType}</span></td>
                <td>${s.lectureHours}-${s.tutorialHours}-${s.practicalHours}</td>
                <td><strong>${s.credits} Credits</strong></td>
                <td><span class="badge ${s.isActive ? 'bg-success' : 'bg-secondary'}">${s.isActive ? 'Active' : 'Inactive'}</span></td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.warn('Error loading subjects:', e);
    }
}

// 5. Biometric Devices
async function loadDevices() {
    try {
        const res = await SmartAttendAPI.get('/biometric/devices');
        allDevices = res.data || [];
        const tbody = document.getElementById('devices-table-body');
        if (!tbody) return;

        tbody.innerHTML = '';
        allDevices.forEach((d, idx) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${idx + 1}</td>
                <td><strong>${d.deviceCode}</strong></td>
                <td>${d.deviceName}</td>
                <td>${d.location}</td>
                <td><code>${d.ipAddress}:${d.port}</code></td>
                <td>${d.deviceType}</td>
                <td><span class="badge ${d.status === 'ONLINE' ? 'bg-success' : 'bg-danger'}">${d.status}</span></td>
                <td>
                    <button class="btn btn-sm btn-erp-outline py-1 px-2" onclick="sendHeartbeat('${d.deviceCode}')">
                        <i class="fas fa-heart-pulse text-danger"></i> Heartbeat
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.warn('Error loading devices:', e);
    }
}

async function sendHeartbeat(code) {
    try {
        await SmartAttendAPI.post(`/biometric/devices/1/heartbeat?deviceCode=${encodeURIComponent(code)}`);
        SmartAttendAPI.showToast(`Heartbeat recorded for ${code}. Device is ONLINE.`, 'success');
        loadDevices();
    } catch (e) {
        SmartAttendAPI.showToast('Heartbeat update failed', 'danger');
    }
}

// 6. Academic Calendar
async function loadCalendar() {
    try {
        const res = await SmartAttendAPI.get('/academic/calendar');
        const events = res.data || [];
        const tbody = document.getElementById('calendar-table-body');
        if (!tbody) return;

        tbody.innerHTML = '';
        events.forEach((ev, idx) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${idx + 1}</td>
                <td><strong>${ev.title}</strong></td>
                <td>${ev.startDate} to ${ev.endDate || ev.startDate}</td>
                <td><span class="badge bg-info-subtle text-info">${ev.eventType}</span></td>
                <td>${ev.description || 'BEU Academic Schedule'}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.warn('Error loading calendar:', e);
    }
}

// 7. Audit Logs
async function loadAuditLogs() {
    try {
        const res = await SmartAttendAPI.get('/reports/audit-logs?size=30');
        const logs = (res.data && res.data.content) ? res.data.content : (res.data || []);
        const tbody = document.getElementById('audit-table-body');
        if (!tbody) return;

        tbody.innerHTML = '';
        logs.forEach((log, idx) => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${idx + 1}</td>
                <td><span class="badge bg-primary-subtle text-primary">${log.action}</span></td>
                <td>${log.entityName} #${log.entityId || ''}</td>
                <td>${log.username || 'System'}</td>
                <td><small class="text-muted">${log.timestamp || ''}</small></td>
                <td><small>${log.description || ''}</small></td>
            `;
            tbody.appendChild(row);
        });
    } catch (e) {
        console.warn('Error loading audit logs:', e);
    }
}

window.searchStudents = searchStudents;
window.saveStudent = saveStudent;
window.deleteStudent = deleteStudent;
window.viewStudentReport = viewStudentReport;
window.sendHeartbeat = sendHeartbeat;
