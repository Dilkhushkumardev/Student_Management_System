-- V2__seed_roles.sql
-- Default Roles and Initial Administrative & Demo Accounts

INSERT INTO roles (id, name, description) VALUES
(1, 'ROLE_SUPER_ADMIN', 'Super Administrator with full system privileges and audit management'),
(2, 'ROLE_ADMIN', 'Administrator for academic, student, faculty, and timetable operations'),
(3, 'ROLE_FACULTY', 'Faculty member for attendance recording, session management, and reports'),
(4, 'ROLE_STUDENT', 'Student for viewing subject-wise attendance, shortage calculator, and alerts');

-- Default Passwords (all hashed with BCrypt):
-- admin / Admin@123 -> $2a$10$y5UeN8j7bB1c9rGz0Y3ahe4T6mF9xL1kO8vQ2wE4rT6yU8iO0pAsW (verified dynamically in DataInitializer)
-- faculty / Faculty@123
-- student / Student@123

INSERT INTO users (id, username, password, full_name, email, phone, is_active) VALUES
(1, 'admin', '$2a$10$e7mK/cZpX9JpWwK7mE3jkeW9G8r.0Y2iP5sK1bX4zT8nL6mO3pAsW', 'System Administrator', 'admin@smartattend.beu.edu.in', '9876543210', TRUE),
(2, 'faculty', '$2a$10$e7mK/cZpX9JpWwK7mE3jkeW9G8r.0Y2iP5sK1bX4zT8nL6mO3pAsW', 'Dr. Rajesh Kumar Verma', 'rverma.cse@smartattend.beu.edu.in', '9876543211', TRUE),
(3, 'faculty2', '$2a$10$e7mK/cZpX9JpWwK7mE3jkeW9G8r.0Y2iP5sK1bX4zT8nL6mO3pAsW', 'Prof. Anjali Sharma', 'asharma.phy@smartattend.beu.edu.in', '9876543212', TRUE),
(4, 'student', '$2a$10$e7mK/cZpX9JpWwK7mE3jkeW9G8r.0Y2iP5sK1bX4zT8nL6mO3pAsW', 'Dilkhush Kumar', 'dilkhush.26105157014@smartattend.beu.edu.in', '9876543214', TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- Super Admin
(1, 2), -- Admin
(2, 3), -- Faculty 1
(3, 3), -- Faculty 2
(4, 4); -- Student (Dilkhush Kumar)
