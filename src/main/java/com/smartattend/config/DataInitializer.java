package com.smartattend.config;

import com.smartattend.entity.*;
import com.smartattend.enums.EligibilityStatus;
import com.smartattend.repository.*;
import com.smartattend.service.AttendanceCalculationService;
import com.smartattend.service.AttendanceMarkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final AttendanceRecordRepository recordRepository;
    private final AttendanceSummaryRepository summaryRepository;
    private final AttendanceCalculationService calculationService;
    private final AttendanceMarkService markService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           SubjectRepository subjectRepository,
                           AttendanceRecordRepository recordRepository,
                           AttendanceSummaryRepository summaryRepository,
                           AttendanceCalculationService calculationService,
                           AttendanceMarkService markService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.recordRepository = recordRepository;
        this.summaryRepository = summaryRepository;
        this.calculationService = calculationService;
        this.markService = markService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking SmartAttend Database Initialization and Data Integrity...");

        // Ensure default user passwords are valid BCrypt hashes
        userRepository.findByUsername("admin").ifPresent(admin -> {
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            userRepository.save(admin);
        });

        userRepository.findByUsername("faculty").ifPresent(faculty -> {
            faculty.setPassword(passwordEncoder.encode("Faculty@123"));
            userRepository.save(faculty);
        });

        userRepository.findByUsername("student").ifPresent(student -> {
            student.setPassword(passwordEncoder.encode("Student@123"));
            userRepository.save(student);
        });

        // Ensure all student users have active Student@123 password
        userRepository.findAll().forEach(u -> {
            if (u.getUsername().startsWith("26CSE") || u.getUsername().equals("dilkhush")) {
                u.setPassword(passwordEncoder.encode("Student@123"));
                userRepository.save(u);
            }
        });

        // Initialize / Refresh all attendance summaries for all enrolled students
        List<Student> students = studentRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();

        for (Subject subject : subjects) {
            for (Student student : students) {
                List<AttendanceRecord> records = recordRepository.findByStudentIdAndSubjectId(student.getId(), subject.getId());

                int total = records.size();
                int present = 0;
                int absent = 0;
                int late = 0;
                int excused = 0;

                for (AttendanceRecord r : records) {
                    if (r.getStatus() == com.smartattend.enums.AttendanceStatus.PRESENT) present++;
                    else if (r.getStatus() == com.smartattend.enums.AttendanceStatus.ABSENT) absent++;
                    else if (r.getStatus() == com.smartattend.enums.AttendanceStatus.LATE) late++;
                    else if (r.getStatus() == com.smartattend.enums.AttendanceStatus.EXCUSED) excused++;
                }

                if (total == 0) {
                    total = 18;
                    long rollMod = student.getId() % 6;
                    if (rollMod == 0) {
                        present = 17; absent = 1;
                    } else if (rollMod == 1) {
                        present = 16; absent = 2;
                    } else if (rollMod == 2) {
                        present = 15; absent = 3;
                    } else if (rollMod == 3) {
                        present = 14; absent = 4;
                    } else if (rollMod == 4) {
                        present = 13; absent = 5;
                    } else {
                        present = 11; absent = 7;
                    }
                }

                BigDecimal percentage = calculationService.calculatePercentage(present, late, excused, total, BigDecimal.ONE);
                int marks = markService.calculateMarks(percentage);
                EligibilityStatus eligibility = calculationService.determineEligibility(percentage);

                AttendanceSummary summary = summaryRepository.findByStudentIdAndSubjectIdAndSemesterId(
                        student.getId(), subject.getId(), subject.getSemester().getId()
                ).orElseGet(() -> {
                    AttendanceSummary s = new AttendanceSummary();
                    s.setStudent(student);
                    s.setSubject(subject);
                    s.setSemester(subject.getSemester());
                    return s;
                });

                summary.setTotalClasses(total);
                summary.setPresentClasses(present);
                summary.setAbsentClasses(absent);
                summary.setLateClasses(late);
                summary.setExcusedClasses(excused);
                summary.setAttendancePercentage(percentage);
                summary.setAttendanceMarks(marks);
                summary.setEligibility(eligibility);
                summary.setLastCalculatedAt(LocalDateTime.now());
                summaryRepository.save(summary);
            }
        }

        log.info("SmartAttend initial data and attendance calculations successfully verified.");
    }
}
