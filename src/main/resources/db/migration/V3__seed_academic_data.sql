-- V3__seed_academic_data.sql
-- Departments, Branches, Batches, Semesters, Sections, Biometric Devices, and Academic Calendar (BEU Official)

-- Departments
INSERT INTO departments (id, code, name, description, is_active) VALUES
(1, 'CSE', 'Computer Science & Engineering', 'Department of Computer Science & Engineering', TRUE),
(2, 'ECE', 'Electronics & Communication Engineering', 'Department of Electronics & Communication Engineering', TRUE),
(3, 'EEE', 'Electrical & Electronics Engineering', 'Department of Electrical & Electronics Engineering', TRUE),
(4, 'ME', 'Mechanical Engineering', 'Department of Mechanical Engineering', TRUE),
(5, 'CE', 'Civil Engineering', 'Department of Civil Engineering', TRUE);

-- Branches
INSERT INTO branches (id, code, name, department_id, is_active) VALUES
(1, '105', 'Computer Science & Engineering', 1, TRUE),
(2, '101', 'Civil Engineering', 5, TRUE),
(3, '102', 'Mechanical Engineering', 4, TRUE),
(4, '118', 'Electrical & Electronics Engineering', 3, TRUE);

-- Batches
INSERT INTO batches (id, name, start_year, end_year, is_active) VALUES
(1, '2026–2030', 2026, 2030, TRUE),
(2, '2025–2029', 2025, 2029, TRUE);

-- Semesters
INSERT INTO semesters (id, semester_number, name, academic_session, is_current, is_active) VALUES
(1, 1, '1st Semester (Autumn 2026)', '2026–2030', TRUE, TRUE),
(2, 2, '2nd Semester (Spring 2027)', '2026–2030', FALSE, TRUE);

-- Sections
INSERT INTO sections (id, name, batch_id, semester_id, department_id, academic_session, is_active) VALUES
(1, 'Section A', 1, 1, 1, '2026–2030', TRUE),
(2, 'Section B', 1, 1, 1, '2026–2030', TRUE);

-- Faculty Profiles
INSERT INTO faculty (id, user_id, employee_id, name, email, mobile, department_id, designation, status) VALUES
(1, 2, 'EMP-BEU-CSE-001', 'Dr. Rajesh Kumar Verma', 'rverma.cse@smartattend.beu.edu.in', '9876543211', 1, 'Professor & HOD', 'ACTIVE'),
(2, 3, 'EMP-BEU-PHY-002', 'Prof. Anjali Sharma', 'asharma.phy@smartattend.beu.edu.in', '9876543212', 1, 'Assistant Professor', 'ACTIVE');

-- Biometric Devices
INSERT INTO biometric_devices (id, device_code, device_name, serial_number, ip_address, port, location, device_type, status, last_heartbeat, last_sync_time, api_key, is_active) VALUES
(1, 'BIO-CSE-001', 'CSE Smart Biometric Terminal 1', 'ST-BIO-2026-001', '192.168.1.101', 4370, 'Room 201 - CSE Block A', 'FINGERPRINT', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'BIO_KEY_2026_001_SECRET', TRUE),
(2, 'BIO-CSE-002', 'CSE Facial & RFID Terminal 2', 'ST-BIO-2026-002', '192.168.1.102', 4370, 'Room 202 - AI & Computing Lab', 'FACE', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'BIO_KEY_2026_002_SECRET', TRUE),
(3, 'BIO-GATE-001', 'Main Academic Gate Terminal', 'ST-BIO-2026-003', '192.168.1.103', 4370, 'Central Academic Building Entrance', 'HYBRID', 'ONLINE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'BIO_KEY_2026_003_SECRET', TRUE);

-- Academic Calendar (Official Bihar Engineering University, Patna Holiday List 2026)
INSERT INTO academic_calendar (id, event_title, event_type, start_date, end_date, total_days, day_name, description, academic_session, is_active) VALUES
(1, 'New Year Celebration', 'HOLIDAY', '2026-01-01', '2026-01-01', 1, 'Thursday', 'University Holiday', '2026–2030', TRUE),
(2, 'Makar Sankranti', 'HOLIDAY', '2026-01-14', '2026-01-14', 1, 'Wednesday', 'Gazetted Holiday', '2026–2030', TRUE),
(3, 'Basant Panchami / Saraswati Puja', 'HOLIDAY', '2026-01-23', '2026-01-23', 1, 'Friday', 'Gazetted Holiday', '2026–2030', TRUE),
(4, 'Republic Day', 'HOLIDAY', '2026-01-26', '2026-01-26', 1, 'Monday', 'National Holiday', '2026–2030', TRUE),
(5, 'Shab-e-Barat', 'HOLIDAY', '2026-02-04', '2026-02-04', 1, 'Wednesday', 'Gazetted Holiday', '2026–2030', TRUE),
(6, 'Mahashivratri', 'HOLIDAY', '2026-02-15', '2026-02-15', 1, 'Sunday', 'Gazetted Holiday', '2026–2030', TRUE),
(7, 'Holika Dahan / Holi', 'HOLIDAY', '2026-03-02', '2026-03-04', 3, 'Monday-Wednesday', 'Festival of Colors Break', '2026–2030', TRUE),
(8, 'Eid-ul-Fitr', 'HOLIDAY', '2026-03-21', '2026-03-21', 1, 'Saturday', 'Gazetted Holiday', '2026–2030', TRUE),
(9, 'Bihar Diwas', 'HOLIDAY', '2026-03-22', '2026-03-22', 1, 'Sunday', 'State Holiday', '2026–2030', TRUE),
(10, 'Samrat Ashok Jayanti', 'HOLIDAY', '2026-03-26', '2026-03-26', 1, 'Thursday', 'Gazetted Holiday', '2026–2030', TRUE),
(11, 'Ram Navami', 'HOLIDAY', '2026-03-27', '2026-03-27', 1, 'Friday', 'Gazetted Holiday', '2026–2030', TRUE),
(12, 'Mahavir Jayanti', 'HOLIDAY', '2026-03-31', '2026-03-31', 1, 'Tuesday', 'Gazetted Holiday', '2026–2030', TRUE),
(13, 'Good Friday', 'HOLIDAY', '2026-04-03', '2026-04-03', 1, 'Friday', 'Gazetted Holiday', '2026–2030', TRUE),
(14, 'Dr. B.R. Ambedkar Jayanti', 'HOLIDAY', '2026-04-14', '2026-04-14', 1, 'Tuesday', 'Gazetted Holiday', '2026–2030', TRUE),
(15, 'Veer Kunwar Singh Jayanti', 'HOLIDAY', '2026-04-23', '2026-04-23', 1, 'Thursday', 'Gazetted Holiday', '2026–2030', TRUE),
(16, 'Janaki Navami', 'HOLIDAY', '2026-04-25', '2026-04-25', 1, 'Saturday', 'Gazetted Holiday', '2026–2030', TRUE),
(17, 'May Day / Buddha Purnima', 'HOLIDAY', '2026-05-01', '2026-05-01', 1, 'Friday', 'Gazetted Holiday', '2026–2030', TRUE),
(18, 'Eid-ul-Zoha (Bakrid)', 'HOLIDAY', '2026-05-28', '2026-05-28', 1, 'Thursday', 'Gazetted Holiday', '2026–2030', TRUE),
(19, 'Summer Vacation', 'HOLIDAY', '2026-06-01', '2026-06-30', 30, 'Monday-Tuesday', 'Annual Summer Break', '2026–2030', TRUE),
(20, 'Independence Day', 'HOLIDAY', '2026-08-15', '2026-08-15', 1, 'Saturday', 'National Holiday', '2026–2030', TRUE),
(21, 'Raksha Bandhan', 'HOLIDAY', '2026-08-28', '2026-08-28', 1, 'Friday', 'Gazetted Holiday', '2026–2030', TRUE),
(22, 'Shri Krishna Janmashtami', 'HOLIDAY', '2026-09-04', '2026-09-04', 1, 'Friday', 'Gazetted Holiday', '2026–2030', TRUE),
(23, 'Mahatma Gandhi Jayanti', 'HOLIDAY', '2026-10-02', '2026-10-02', 1, 'Friday', 'National Holiday', '2026–2030', TRUE),
(24, 'Durga Puja Break', 'HOLIDAY', '2026-10-17', '2026-10-20', 4, 'Saturday-Tuesday', 'Durga Puja Festival Break', '2026–2030', TRUE),
(25, 'Deepawali & Chhath Puja Break', 'HOLIDAY', '2026-11-08', '2026-11-16', 9, 'Sunday-Monday', 'Diwali, Bhai Dooj & Chhath Mahaparv Break', '2026–2030', TRUE),
(26, 'Guru Nanak Jayanti', 'HOLIDAY', '2026-11-24', '2026-11-24', 1, 'Tuesday', 'Gazetted Holiday', '2026–2030', TRUE),
(27, 'Christmas & Winter Vacation', 'HOLIDAY', '2026-12-25', '2026-12-31', 7, 'Friday-Thursday', 'Winter Break', '2026–2030', TRUE);
