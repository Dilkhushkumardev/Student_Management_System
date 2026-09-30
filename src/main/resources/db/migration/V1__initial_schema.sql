-- V1__initial_schema.sql
-- SmartAttend Database Schema for MySQL 8.x and H2 (MySQL Mode)

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS branches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    department_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_branch_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    start_year INT NOT NULL,
    end_year INT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS semesters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    semester_number INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    academic_session VARCHAR(50) NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE(semester_number, academic_session)
);

CREATE TABLE IF NOT EXISTS sections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    batch_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    academic_session VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_section_batch FOREIGN KEY (batch_id) REFERENCES batches(id) ON DELETE CASCADE,
    CONSTRAINT fk_section_semester FOREIGN KEY (semester_id) REFERENCES semesters(id) ON DELETE CASCADE,
    CONSTRAINT fk_section_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS faculty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    mobile VARCHAR(20),
    department_id BIGINT NOT NULL,
    designation VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_faculty_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_faculty_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    university_reg_no VARCHAR(50) NOT NULL UNIQUE,
    roll_no VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    father_name VARCHAR(150),
    mother_name VARCHAR(150),
    email VARCHAR(150) NOT NULL UNIQUE,
    mobile VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10),
    department_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    admission_year INT NOT NULL,
    profile_photo VARCHAR(255),
    biometric_id VARCHAR(50) UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_student_department FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_student_branch FOREIGN KEY (branch_id) REFERENCES branches(id),
    CONSTRAINT fk_student_batch FOREIGN KEY (batch_id) REFERENCES batches(id),
    CONSTRAINT fk_student_semester FOREIGN KEY (semester_id) REFERENCES semesters(id),
    CONSTRAINT fk_student_section FOREIGN KEY (section_id) REFERENCES sections(id)
);

CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(30) NOT NULL UNIQUE,
    subject_name VARCHAR(150) NOT NULL,
    subject_type VARCHAR(20) NOT NULL DEFAULT 'THEORY',
    lecture_hours INT NOT NULL DEFAULT 3,
    tutorial_hours INT NOT NULL DEFAULT 0,
    practical_hours INT NOT NULL DEFAULT 0,
    credits DECIMAL(4, 2) NOT NULL DEFAULT 3.0,
    max_internal_marks INT NOT NULL DEFAULT 30,
    max_ese_marks INT NOT NULL DEFAULT 70,
    semester_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    academic_session VARCHAR(50) NOT NULL,
    attendance_required BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subject_semester FOREIGN KEY (semester_id) REFERENCES semesters(id),
    CONSTRAINT fk_subject_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS faculty_subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    faculty_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    academic_session VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE(faculty_id, subject_id, section_id, academic_session),
    CONSTRAINT fk_facsub_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE CASCADE,
    CONSTRAINT fk_facsub_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT fk_facsub_section FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    academic_session VARCHAR(50) NOT NULL,
    enrollment_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, subject_id, semester_id),
    CONSTRAINT fk_enroll_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_enroll_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT fk_enroll_section FOREIGN KEY (section_id) REFERENCES sections(id),
    CONSTRAINT fk_enroll_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);

CREATE TABLE IF NOT EXISTS timetable (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    period_number INT NOT NULL,
    subject_id BIGINT NOT NULL,
    faculty_id BIGINT NOT NULL,
    room_no VARCHAR(50) NOT NULL,
    section_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    academic_session VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_tt_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_tt_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id),
    CONSTRAINT fk_tt_section FOREIGN KEY (section_id) REFERENCES sections(id),
    CONSTRAINT fk_tt_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);

CREATE TABLE IF NOT EXISTS attendance_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_code VARCHAR(64) NOT NULL UNIQUE,
    subject_id BIGINT NOT NULL,
    faculty_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    period_number INT NOT NULL DEFAULT 1,
    room_no VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    verification_mode VARCHAR(20) NOT NULL DEFAULT 'MANUAL',
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    locked_at TIMESTAMP NULL,
    locked_by_id BIGINT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attsess_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_attsess_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id),
    CONSTRAINT fk_attsess_section FOREIGN KEY (section_id) REFERENCES sections(id),
    CONSTRAINT fk_attsess_lockedby FOREIGN KEY (locked_by_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS biometric_devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_code VARCHAR(50) NOT NULL UNIQUE,
    device_name VARCHAR(150) NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    ip_address VARCHAR(50),
    port INT DEFAULT 80,
    location VARCHAR(150),
    device_type VARCHAR(30) NOT NULL DEFAULT 'FINGERPRINT',
    status VARCHAR(20) NOT NULL DEFAULT 'ONLINE',
    last_heartbeat TIMESTAMP NULL,
    last_sync_time TIMESTAMP NULL,
    api_key VARCHAR(100) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS biometric_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT UNIQUE,
    faculty_id BIGINT UNIQUE,
    biometric_id VARCHAR(50) NOT NULL UNIQUE,
    device_user_id VARCHAR(50) NOT NULL,
    enrollment_status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    template_type VARCHAR(30) DEFAULT 'FINGERPRINT',
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_verified_at TIMESTAMP NULL,
    CONSTRAINT fk_biouser_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_biouser_faculty FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS biometric_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    biometric_user_id VARCHAR(50) NOT NULL,
    student_id BIGINT NULL,
    attendance_session_id BIGINT NULL,
    verification_type VARCHAR(30) NOT NULL DEFAULT 'FINGERPRINT',
    verification_result VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
    event_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    device_location VARCHAR(150),
    raw_payload TEXT,
    is_processed BOOLEAN NOT NULL DEFAULT FALSE,
    processed_at TIMESTAMP NULL,
    CONSTRAINT fk_bioevt_device FOREIGN KEY (device_id) REFERENCES biometric_devices(id),
    CONSTRAINT fk_bioevt_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_bioevt_session FOREIGN KEY (attendance_session_id) REFERENCES attendance_sessions(id)
);

CREATE TABLE IF NOT EXISTS attendance_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attendance_session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PRESENT',
    method VARCHAR(30) NOT NULL DEFAULT 'MANUAL',
    biometric_event_id BIGINT NULL,
    remarks VARCHAR(255),
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    marked_by_id BIGINT NULL,
    is_admin_override BOOLEAN NOT NULL DEFAULT FALSE,
    override_reason VARCHAR(255),
    override_by_id BIGINT NULL,
    override_at TIMESTAMP NULL,
    UNIQUE(attendance_session_id, student_id),
    CONSTRAINT fk_attrec_session FOREIGN KEY (attendance_session_id) REFERENCES attendance_sessions(id) ON DELETE CASCADE,
    CONSTRAINT fk_attrec_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_attrec_bioevt FOREIGN KEY (biometric_event_id) REFERENCES biometric_events(id),
    CONSTRAINT fk_attrec_markedby FOREIGN KEY (marked_by_id) REFERENCES users(id),
    CONSTRAINT fk_attrec_overrideby FOREIGN KEY (override_by_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS attendance_summary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    total_classes INT NOT NULL DEFAULT 0,
    present_classes INT NOT NULL DEFAULT 0,
    absent_classes INT NOT NULL DEFAULT 0,
    late_classes INT NOT NULL DEFAULT 0,
    excused_classes INT NOT NULL DEFAULT 0,
    attendance_percentage DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    attendance_marks INT NOT NULL DEFAULT 0,
    eligibility VARCHAR(30) NOT NULL DEFAULT 'SHORTAGE',
    last_calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, subject_id, semester_id),
    CONSTRAINT fk_attsum_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_attsum_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT fk_attsum_semester FOREIGN KEY (semester_id) REFERENCES semesters(id)
);

CREATE TABLE IF NOT EXISTS attendance_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL UNIQUE,
    min_percentage_required DECIMAL(5, 2) NOT NULL DEFAULT 75.00,
    condonation_percentage_allowed DECIMAL(5, 2) NOT NULL DEFAULT 15.00,
    late_as_present_weight DECIMAL(3, 2) NOT NULL DEFAULT 1.00,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS attendance_mark_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    min_percentage DECIMAL(5, 2) NOT NULL,
    max_percentage DECIMAL(5, 2) NOT NULL,
    marks_awarded INT NOT NULL,
    rule_description VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS academic_calendar (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_title VARCHAR(150) NOT NULL,
    event_type VARCHAR(30) NOT NULL DEFAULT 'HOLIDAY',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days INT NOT NULL DEFAULT 1,
    day_name VARCHAR(50),
    description VARCHAR(255),
    academic_session VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    username VARCHAR(100) NOT NULL,
    user_role VARCHAR(50),
    action VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(50),
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(50),
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    notification_type VARCHAR(50) NOT NULL DEFAULT 'INFO',
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    reference_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_reftok_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for high performance querying
CREATE INDEX idx_student_regno ON students(university_reg_no);
CREATE INDEX idx_student_rollno ON students(roll_no);
CREATE INDEX idx_student_bioid ON students(biometric_id);
CREATE INDEX idx_student_section ON students(section_id);
CREATE INDEX idx_attrec_session ON attendance_records(attendance_session_id);
CREATE INDEX idx_attrec_student ON attendance_records(student_id);
CREATE INDEX idx_attsum_stud_sub ON attendance_summary(student_id, subject_id);
CREATE INDEX idx_attsess_date_sec ON attendance_sessions(session_date, section_id);
CREATE INDEX idx_bioevt_bio_user ON biometric_events(biometric_user_id);
CREATE INDEX idx_bioevt_processed ON biometric_events(is_processed);
CREATE INDEX idx_audit_timestamp ON audit_logs(timestamp);
