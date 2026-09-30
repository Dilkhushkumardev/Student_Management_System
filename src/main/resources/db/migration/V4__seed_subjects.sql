-- V4__seed_subjects.sql
-- B.Tech 1st Semester Group-A Subjects and Faculty Assignments (BEU Syllabus)

INSERT INTO subjects (id, course_code, subject_name, subject_type, lecture_hours, tutorial_hours, practical_hours, credits, max_internal_marks, max_ese_marks, semester_id, department_id, academic_session, attendance_required, is_active) VALUES
(1, '100102', 'Engineering Mathematics - I', 'THEORY', 3, 0, 0, 3.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(2, '100104', 'Engineering Physics', 'THEORY', 3, 0, 0, 3.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(3, '100105', 'Introduction to AI', 'THEORY', 3, 0, 0, 3.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(4, '100108', 'Computer Fundamentals & Emerging Technologies', 'THEORY', 3, 0, 0, 3.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(5, '100109', 'Universal Human Values', 'THEORY', 2, 0, 0, 2.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(6, '100110', 'Essence of Indian Constitution', 'THEORY', 3, 0, 0, 0.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(7, '100111', 'Basics of Electrical & Electronics Engineering', 'THEORY', 3, 0, 0, 3.0, 30, 70, 1, 1, '2026–2030', TRUE, TRUE),
(8, '100104P', 'Engineering Physics Lab', 'PRACTICAL', 0, 0, 2, 1.0, 20, 30, 1, 1, '2026–2030', TRUE, TRUE),
(9, '100111P', 'Basics of Electrical & Electronics Engineering Lab', 'PRACTICAL', 0, 0, 2, 1.0, 20, 30, 1, 1, '2026–2030', TRUE, TRUE),
(10, '100112P', 'Programming for Problem Solving Lab', 'PRACTICAL', 0, 0, 2, 1.0, 20, 30, 1, 1, '2026–2030', TRUE, TRUE);

-- Faculty Subject Section Mappings
INSERT INTO faculty_subjects (id, faculty_id, subject_id, section_id, academic_session, is_active) VALUES
(1, 1, 3, 1, '2026–2030', TRUE),  -- Dr. Rajesh Verma -> Intro to AI (Sec A)
(2, 1, 4, 1, '2026–2030', TRUE),  -- Dr. Rajesh Verma -> CFET (Sec A)
(3, 1, 10, 1, '2026–2030', TRUE), -- Dr. Rajesh Verma -> PPS Lab (Sec A)
(4, 2, 1, 1, '2026–2030', TRUE),  -- Prof. Anjali Sharma -> Engg Math-I (Sec A)
(5, 2, 2, 1, '2026–2030', TRUE),  -- Prof. Anjali Sharma -> Engg Physics (Sec A)
(6, 2, 8, 1, '2026–2030', TRUE),  -- Prof. Anjali Sharma -> Physics Lab (Sec A)
(7, 2, 7, 1, '2026–2030', TRUE),  -- Prof. Anjali Sharma -> BEEE (Sec A)
(8, 2, 9, 1, '2026–2030', TRUE),  -- Prof. Anjali Sharma -> BEEE Lab (Sec A)
(9, 2, 5, 1, '2026–2030', TRUE),  -- Prof. Anjali Sharma -> UHV (Sec A)
(10, 2, 6, 1, '2026–2030', TRUE); -- Prof. Anjali Sharma -> Constitution (Sec A)
