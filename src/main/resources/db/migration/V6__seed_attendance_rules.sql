-- V6__seed_attendance_rules.sql
-- Attendance Rules, BEU 5-Mark Slab Rules, Timetable, Sample Sessions, Records & Summaries

-- Attendance Rules (BEU Standard)
INSERT INTO attendance_rules (id, rule_name, min_percentage_required, condonation_percentage_allowed, late_as_present_weight, is_active, description) VALUES
(1, 'BEU Regulation 75% Rule', 75.00, 15.00, 1.00, TRUE, 'Bihar Engineering University 75% mandatory attendance requirement with max 15% medical condonation as per Appendix-I');

-- BEU Attendance Mark Slabs (Regulation Clause 6.2 and BEU Notice dated 12 March 2026)
INSERT INTO attendance_mark_rules (id, min_percentage, max_percentage, marks_awarded, rule_description, is_active) VALUES
(1, 0.00, 74.99, 0, 'Below 75% attendance gets 0 marks out of 5 in Internal Assessment', TRUE),
(2, 75.00, 80.99, 1, '75% to 80.99% attendance gets 1 mark out of 5 in Internal Assessment', TRUE),
(3, 81.00, 85.99, 2, '81% to 85.99% attendance gets 2 marks out of 5 in Internal Assessment', TRUE),
(4, 86.00, 90.99, 3, '86% to 90.99% attendance gets 3 marks out of 5 in Internal Assessment', TRUE),
(5, 91.00, 95.99, 4, '91% to 95.99% attendance gets 4 marks out of 5 in Internal Assessment', TRUE),
(6, 96.00, 100.00, 5, '96% to 100% attendance gets 5 marks out of 5 in Internal Assessment', TRUE);

-- Timetable for CSE 1st Sem Section A
INSERT INTO timetable (id, day_of_week, start_time, end_time, period_number, subject_id, faculty_id, room_no, section_id, semester_id, academic_session, is_active) VALUES
(1, 'MONDAY', '09:00:00', '10:00:00', 1, 1, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(2, 'MONDAY', '10:00:00', '11:00:00', 2, 2, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(3, 'MONDAY', '11:15:00', '12:15:00', 3, 3, 1, 'Room 201', 1, 1, '2026–2030', TRUE),
(4, 'MONDAY', '12:15:00', '13:15:00', 4, 4, 1, 'Room 201', 1, 1, '2026–2030', TRUE),
(5, 'MONDAY', '14:00:00', '16:00:00', 5, 10, 1, 'Lab 1 - CSE', 1, 1, '2026–2030', TRUE),

(6, 'TUESDAY', '09:00:00', '10:00:00', 1, 7, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(7, 'TUESDAY', '10:00:00', '11:00:00', 2, 1, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(8, 'TUESDAY', '11:15:00', '12:15:00', 3, 5, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(9, 'TUESDAY', '14:00:00', '16:00:00', 5, 8, 2, 'Physics Lab', 1, 1, '2026–2030', TRUE),

(10, 'WEDNESDAY', '09:00:00', '10:00:00', 1, 2, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(11, 'WEDNESDAY', '10:00:00', '11:00:00', 2, 3, 1, 'Room 201', 1, 1, '2026–2030', TRUE),
(12, 'WEDNESDAY', '11:15:00', '12:15:00', 3, 4, 1, 'Room 201', 1, 1, '2026–2030', TRUE),
(13, 'WEDNESDAY', '14:00:00', '16:00:00', 5, 9, 2, 'BEEE Lab', 1, 1, '2026–2030', TRUE),

(14, 'THURSDAY', '09:00:00', '10:00:00', 1, 1, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(15, 'THURSDAY', '10:00:00', '11:00:00', 2, 7, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(16, 'THURSDAY', '11:15:00', '12:15:00', 3, 6, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(17, 'THURSDAY', '14:00:00', '16:00:00', 5, 10, 1, 'Lab 1 - CSE', 1, 1, '2026–2030', TRUE),

(18, 'FRIDAY', '09:00:00', '10:00:00', 1, 3, 1, 'Room 201', 1, 1, '2026–2030', TRUE),
(19, 'FRIDAY', '10:00:00', '11:00:00', 2, 4, 1, 'Room 201', 1, 1, '2026–2030', TRUE),
(20, 'FRIDAY', '11:15:00', '12:15:00', 3, 5, 2, 'Room 201', 1, 1, '2026–2030', TRUE),
(21, 'FRIDAY', '14:00:00', '15:00:00', 4, 2, 2, 'Room 201', 1, 1, '2026–2030', TRUE);

-- Sample Attendance Sessions (Locked historical sessions + 1 Active session today)
INSERT INTO attendance_sessions (id, session_code, subject_id, faculty_id, section_id, session_date, start_time, end_time, period_number, room_no, status, verification_mode, is_locked, locked_at, locked_by_id, notes) VALUES
(1, 'SESS-20260920-MATH1-P1', 1, 2, 1, '2026-09-20', '09:00:00', '10:00:00', 1, 'Room 201', 'LOCKED', 'BIOMETRIC', TRUE, '2026-09-20 10:05:00', 2, 'Calculus & Matrices Session 1'),
(2, 'SESS-20260921-AI-P3', 3, 1, 1, '2026-09-21', '11:15:00', '12:15:00', 3, 'Room 201', 'LOCKED', 'BIOMETRIC', TRUE, '2026-09-21 12:20:00', 2, 'Introduction to Intelligent Systems'),
(3, 'SESS-20260922-CFET-P4', 4, 1, 1, '2026-09-22', '12:15:00', '13:15:00', 4, 'Room 201', 'LOCKED', 'BIOMETRIC', TRUE, '2026-09-22 13:20:00', 2, 'Computer Architecture Overview'),
(4, 'SESS-20260923-PHYS-P2', 2, 2, 1, '2026-09-23', '10:00:00', '11:00:00', 2, 'Room 201', 'LOCKED', 'MANUAL', TRUE, '2026-09-23 11:05:00', 3, 'Quantum Mechanics Intro'),
(5, 'SESS-20260924-BEEE-P1', 7, 2, 1, '2026-09-24', '09:00:00', '10:00:00', 1, 'Room 201', 'LOCKED', 'BIOMETRIC', TRUE, '2026-09-24 10:05:00', 3, 'DC Circuit Analysis'),
(6, 'SESS-20260925-PPSLAB-P5', 10, 1, 1, '2026-09-25', '14:00:00', '16:00:00', 5, 'Lab 1 - CSE', 'LOCKED', 'BIOMETRIC', TRUE, '2026-09-25 16:05:00', 2, 'C Programming Lab 1'),
(7, 'SESS-20260929-AI-P3', 3, 1, 1, '2026-09-29', '11:15:00', '12:15:00', 3, 'Room 201', 'ACTIVE', 'BIOMETRIC', FALSE, NULL, NULL, 'Live Demo Attendance Session');

-- Initial Attendance Records for Session 1 (Engg Math-I)
INSERT INTO attendance_records (attendance_session_id, student_id, status, method, marked_at, marked_by_id)
SELECT 1, id, 
       CASE 
         WHEN id IN (5, 9, 14, 22, 28) THEN 'ABSENT'
         WHEN id IN (3, 11) THEN 'LATE'
         ELSE 'PRESENT'
       END,
       'BIOMETRIC', '2026-09-20 09:15:00', 2
FROM students;

-- Initial Attendance Records for Session 2 (Intro to AI)
INSERT INTO attendance_records (attendance_session_id, student_id, status, method, marked_at, marked_by_id)
SELECT 2, id, 
       CASE 
         WHEN id IN (4, 10, 18, 24, 29) THEN 'ABSENT'
         WHEN id IN (6, 15) THEN 'LATE'
         ELSE 'PRESENT'
       END,
       'BIOMETRIC', '2026-09-21 11:25:00', 2
FROM students;

-- Initial Attendance Records for Session 3 (CFET)
INSERT INTO attendance_records (attendance_session_id, student_id, status, method, marked_at, marked_by_id)
SELECT 3, id, 
       CASE 
         WHEN id IN (2, 8, 16, 25, 30) THEN 'ABSENT'
         ELSE 'PRESENT'
       END,
       'BIOMETRIC', '2026-09-22 12:25:00', 2
FROM students;

-- Initial Attendance Records for Session 4 (Engg Physics)
INSERT INTO attendance_records (attendance_session_id, student_id, status, method, marked_at, marked_by_id)
SELECT 4, id, 
       CASE 
         WHEN id IN (7, 13, 21, 27) THEN 'ABSENT'
         ELSE 'PRESENT'
       END,
       'MANUAL', '2026-09-23 10:15:00', 3
FROM students;

-- Initial Attendance Records for Session 5 (BEEE)
INSERT INTO attendance_records (attendance_session_id, student_id, status, method, marked_at, marked_by_id)
SELECT 5, id, 
       CASE 
         WHEN id IN (5, 12, 19, 26) THEN 'ABSENT'
         ELSE 'PRESENT'
       END,
       'BIOMETRIC', '2026-09-24 09:15:00', 3
FROM students;

-- Initial Attendance Records for Session 6 (PPS Lab)
INSERT INTO attendance_records (attendance_session_id, student_id, status, method, marked_at, marked_by_id)
SELECT 6, id, 
       CASE 
         WHEN id IN (9, 17, 23) THEN 'ABSENT'
         ELSE 'PRESENT'
       END,
       'BIOMETRIC', '2026-09-25 14:15:00', 2
FROM students;
