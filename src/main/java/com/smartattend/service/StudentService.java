package com.smartattend.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.smartattend.dto.AttendanceDtoModels.*;
import com.smartattend.dto.ReportDtoModels.*;
import com.smartattend.dto.StudentDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.enums.EligibilityStatus;
import com.smartattend.enums.RoleType;
import com.smartattend.exception.BadRequestException;
import com.smartattend.exception.DuplicateResourceException;
import com.smartattend.exception.ResourceNotFoundException;
import com.smartattend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;
    private final BatchRepository batchRepository;
    private final SemesterRepository semesterRepository;
    private final SectionRepository sectionRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceSummaryRepository summaryRepository;
    private final AttendanceRecordRepository recordRepository;
    private final BiometricEventRepository biometricEventRepository;
    private final AttendanceCalculationService calculationService;
    private final AttendanceMarkService markService;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          DepartmentRepository departmentRepository,
                          BranchRepository branchRepository,
                          BatchRepository batchRepository,
                          SemesterRepository semesterRepository,
                          SectionRepository sectionRepository,
                          SubjectRepository subjectRepository,
                          EnrollmentRepository enrollmentRepository,
                          AttendanceSummaryRepository summaryRepository,
                          AttendanceRecordRepository recordRepository,
                          BiometricEventRepository biometricEventRepository,
                          AttendanceCalculationService calculationService,
                          AttendanceMarkService markService,
                          PasswordEncoder passwordEncoder,
                          AuditService auditService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
        this.batchRepository = batchRepository;
        this.semesterRepository = semesterRepository;
        this.sectionRepository = sectionRepository;
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.summaryRepository = summaryRepository;
        this.recordRepository = recordRepository;
        this.biometricEventRepository = biometricEventRepository;
        this.calculationService = calculationService;
        this.markService = markService;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<StudentDto> getAllStudents(String query, Long departmentId, Long semesterId, Long sectionId, String status, Pageable pageable) {
        return studentRepository.searchStudents(query, departmentId, semesterId, sectionId, status, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return mapToDto(student);
    }

    @Transactional(readOnly = true)
    public StudentDto getStudentByUserId(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found for User ID: " + userId));
        return mapToDto(student);
    }

    @Transactional
    public StudentDto createStudent(StudentCreateRequest request) {
        if (studentRepository.existsByUniversityRegNo(request.getUniversityRegNo())) {
            throw new DuplicateResourceException("Student", "University Registration No", request.getUniversityRegNo());
        }
        if (studentRepository.existsByRollNo(request.getRollNo())) {
            throw new DuplicateResourceException("Student", "Roll No", request.getRollNo());
        }
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student", "Email", request.getEmail());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", request.getBranchId()));
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Batch", "id", request.getBatchId()));
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", request.getSemesterId()));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section", "id", request.getSectionId()));

        Role studentRole = roleRepository.findByName(RoleType.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_STUDENT not found"));

        String username = request.getRollNo();
        String rawPassword = request.getPassword() != null && !request.getPassword().isBlank() ? request.getPassword() : "Student@123";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getMobile());
        user.setIsActive(true);
        user.setRoles(Set.of(studentRole));
        user = userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setUniversityRegNo(request.getUniversityRegNo());
        student.setRollNo(request.getRollNo());
        student.setName(request.getName());
        student.setFatherName(request.getFatherName());
        student.setMotherName(request.getMotherName());
        student.setEmail(request.getEmail());
        student.setMobile(request.getMobile());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        student.setDepartment(department);
        student.setBranch(branch);
        student.setBatch(batch);
        student.setSemester(semester);
        student.setSection(section);
        student.setAdmissionYear(request.getAdmissionYear());
        student.setBiometricId(request.getBiometricId() != null ? request.getBiometricId() : "BIO-" + request.getRollNo());
        student.setStatus("ACTIVE");
        student = studentRepository.save(student);

        // Auto-enroll in all active subjects for this semester
        List<Subject> semesterSubjects = subjectRepository.findBySemesterId(semester.getId());
        List<Enrollment> enrollments = new ArrayList<>();
        for (Subject sub : semesterSubjects) {
            Enrollment e = new Enrollment();
            e.setStudent(student);
            e.setSubject(sub);
            e.setSection(section);
            e.setSemester(semester);
            e.setAcademicSession(sub.getAcademicSession());
            e.setEnrollmentStatus("ACTIVE");
            enrollments.add(e);
        }
        enrollmentRepository.saveAll(enrollments);

        auditService.log("CREATE_STUDENT", "Student", student.getId().toString(), null, student.getRollNo(), null,
                "Student created: " + student.getName() + " (" + student.getRollNo() + ")");

        return mapToDto(student);
    }

    @Transactional
    public StudentDto updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        student.setName(request.getName());
        student.setFatherName(request.getFatherName());
        student.setMotherName(request.getMotherName());
        student.setEmail(request.getEmail());
        student.setMobile(request.getMobile());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setGender(request.getGender());
        if (request.getBiometricId() != null) {
            student.setBiometricId(request.getBiometricId());
        }
        if (request.getStatus() != null) {
            student.setStatus(request.getStatus());
        }
        if (request.getSemesterId() != null) {
            semesterRepository.findById(request.getSemesterId()).ifPresent(student::setSemester);
        }
        if (request.getSectionId() != null) {
            sectionRepository.findById(request.getSectionId()).ifPresent(student::setSection);
        }

        student = studentRepository.save(student);

        User user = student.getUser();
        if (user != null) {
            user.setFullName(student.getName());
            user.setEmail(student.getEmail());
            user.setPhone(student.getMobile());
            userRepository.save(user);
        }

        auditService.log("UPDATE_STUDENT", "Student", student.getId().toString(), null, student.getRollNo(), null,
                "Student updated: " + student.getName());

        return mapToDto(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        student.setStatus("INACTIVE");
        studentRepository.save(student);
        User user = student.getUser();
        if (user != null) {
            user.setIsActive(false);
            userRepository.save(user);
        }

        auditService.log("DEACTIVATE_STUDENT", "Student", id.toString(), "ACTIVE", "INACTIVE", null,
                "Student deactivated: " + student.getRollNo());
    }

    @Transactional
    public Map<String, Object> importStudentsFromCsv(MultipartFile file, Long departmentId, Long branchId, Long batchId, Long semesterId, Long sectionId) {
        int importedCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = reader.readNext(); // skip header
            String[] line;
            int lineNumber = 1;

            while ((line = reader.readNext()) != null) {
                lineNumber++;
                if (line.length < 4 || line[0].isBlank()) continue;

                try {
                    String regNo = line[0].trim();
                    String rollNo = line[1].trim();
                    String name = line[2].trim();
                    String email = line[3].trim();
                    String mobile = line.length > 4 ? line[4].trim() : "98110000" + String.format("%02d", lineNumber % 100);
                    String gender = line.length > 5 ? line[5].trim() : "MALE";
                    String bioId = line.length > 6 && !line[6].isBlank() ? line[6].trim() : "BIO-" + rollNo;

                    if (studentRepository.existsByRollNo(rollNo) || studentRepository.existsByUniversityRegNo(regNo)) {
                        errorCount++;
                        errors.add("Row " + lineNumber + ": Roll " + rollNo + " / Reg " + regNo + " already exists");
                        continue;
                    }

                    StudentCreateRequest req = StudentCreateRequest.builder()
                            .universityRegNo(regNo)
                            .rollNo(rollNo)
                            .name(name)
                            .email(email)
                            .mobile(mobile)
                            .gender(gender)
                            .biometricId(bioId)
                            .departmentId(departmentId != null ? departmentId : 1L)
                            .branchId(branchId != null ? branchId : 1L)
                            .batchId(batchId != null ? batchId : 1L)
                            .semesterId(semesterId != null ? semesterId : 1L)
                            .sectionId(sectionId != null ? sectionId : 1L)
                            .admissionYear(2026)
                            .build();

                    createStudent(req);
                    importedCount++;
                } catch (Exception e) {
                    errorCount++;
                    errors.add("Row " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new BadRequestException("Failed to parse CSV: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("importedCount", importedCount);
        result.put("errorCount", errorCount);
        result.put("errors", errors);
        return result;
    }

    @Transactional(readOnly = true)
    public byte[] exportStudentsCsv(Long sectionId) {
        List<Student> students = sectionId != null ?
                studentRepository.findBySectionIdOrderByRollNoAsc(sectionId) :
                studentRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(out))) {
            writer.writeNext(new String[]{"University Reg No", "Roll No", "Student Name", "Email", "Mobile", "Department", "Semester", "Section", "Biometric ID", "Overall %", "Eligibility", "Status"});

            for (Student s : students) {
                StudentDto dto = mapToDto(s);
                String deptName = s.getDepartment() != null ? s.getDepartment().getName() : "";
                String semName = s.getSemester() != null ? s.getSemester().getName() : "";
                String secName = s.getSection() != null ? s.getSection().getName() : "";
                writer.writeNext(new String[]{
                        s.getUniversityRegNo(),
                        s.getRollNo(),
                        s.getName(),
                        s.getEmail(),
                        s.getMobile() != null ? s.getMobile() : "",
                        deptName,
                        semName,
                        secName,
                        s.getBiometricId() != null ? s.getBiometricId() : "",
                        dto.getOverallAttendancePercentage() != null ? dto.getOverallAttendancePercentage().toString() : "0.00",
                        dto.getEligibilityStatus(),
                        s.getStatus()
                });
            }
        } catch (Exception e) {
            log.error("Failed to generate CSV: {}", e.getMessage());
        }
        return out.toByteArray();
    }

    @Transactional(readOnly = true)
    public StudentDashboardDto getStudentDashboard(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

        List<AttendanceSummary> summaries = summaryRepository.findByStudentId(studentId);
        List<AttendanceCardDto> cards = new ArrayList<>();

        int totalConducted = 0;
        int totalAttended = 0;
        int shortageCount = 0;
        int totalMarks = 0;

        for (AttendanceSummary sum : summaries) {
            AttendanceShortageCalculation calc = calculationService.calculateShortage(
                    sum.getPresentClasses(), sum.getLateClasses(), sum.getTotalClasses()
            );

            if (calc.getCurrentPercentage().compareTo(BigDecimal.valueOf(75.00)) < 0) {
                shortageCount++;
            }
            totalConducted += sum.getTotalClasses();
            totalAttended += (sum.getPresentClasses() + sum.getLateClasses());
            totalMarks += sum.getAttendanceMarks();

            String subName = sum.getSubject() != null ? sum.getSubject().getSubjectName() : "Course";
            String code = sum.getSubject() != null ? sum.getSubject().getCourseCode() : "";
            String type = sum.getSubject() != null ? sum.getSubject().getSubjectType().name() : "THEORY";
            Long subId = sum.getSubject() != null ? sum.getSubject().getId() : null;

            cards.add(AttendanceCardDto.builder()
                    .subjectId(subId)
                    .courseCode(code)
                    .subjectName(subName)
                    .subjectType(type)
                    .attendancePercentage(sum.getAttendancePercentage())
                    .presentClasses(sum.getPresentClasses() + sum.getLateClasses())
                    .absentClasses(sum.getAbsentClasses())
                    .totalClasses(sum.getTotalClasses())
                    .attendanceMarks(sum.getAttendanceMarks())
                    .eligibility(sum.getEligibility())
                    .statusBadgeColor(calc.getStatusBadgeColor())
                    .nextClassSchedule("Scheduled Class")
                    .calculation(calc)
                    .build());
        }

        BigDecimal overall = calculationService.calculatePercentage(totalAttended, 0, 0, totalConducted, BigDecimal.ONE);
        EligibilityStatus overallEligibility = calculationService.determineEligibility(overall);

        List<AttendanceRecord> recentRecords = recordRepository.findByStudentId(studentId).stream()
                .sorted((a, b) -> b.getSession().getSessionDate().compareTo(a.getSession().getSessionDate()))
                .limit(10)
                .toList();

        List<RecentAttendanceItemDto> recentList = recentRecords.stream().map(r -> {
            String subName = r.getSession().getSubject() != null ? r.getSession().getSubject().getSubjectName() : "Course";
            String code = r.getSession().getSubject() != null ? r.getSession().getSubject().getCourseCode() : "";
            String startTime = r.getSession().getStartTime() != null ? r.getSession().getStartTime().toString() : "";
            return RecentAttendanceItemDto.builder()
                    .sessionId(r.getSession().getId())
                    .subjectName(subName)
                    .courseCode(code)
                    .date(r.getSession().getSessionDate())
                    .status(r.getStatus().name())
                    .method(r.getMethod().name())
                    .time(startTime)
                    .build();
        }).collect(Collectors.toList());

        List<BiometricEvent> bioEvents = biometricEventRepository.findByStudentId(studentId).stream()
                .sorted((a, b) -> b.getEventTimestamp().compareTo(a.getEventTimestamp()))
                .limit(10)
                .toList();

        List<BiometricVerificationHistoryDto> bioHistory = bioEvents.stream().map(b -> BiometricVerificationHistoryDto.builder()
                .eventId(b.getId())
                .deviceLocation(b.getDeviceLocation())
                .verificationType(b.getVerificationType())
                .verificationResult(b.getVerificationResult() != null ? b.getVerificationResult().name() : "SUCCESS")
                .timestamp(b.getEventTimestamp())
                .build()).collect(Collectors.toList());

        String deptName = student.getDepartment() != null ? student.getDepartment().getName() : "";
        String secName = student.getSection() != null ? student.getSection().getName() : "";
        String semName = student.getSemester() != null ? student.getSemester().getName() : "";

        return StudentDashboardDto.builder()
                .studentId(student.getId())
                .studentName(student.getName())
                .rollNo(student.getRollNo())
                .universityRegNo(student.getUniversityRegNo())
                .departmentName(deptName)
                .sectionName(secName)
                .semesterName(semName)
                .overallAttendancePercentage(overall)
                .totalSubjectsCount(summaries.size())
                .shortageSubjectsCount(shortageCount)
                .totalMarksEarned(totalMarks)
                .overallEligibility(overallEligibility)
                .subjectCards(cards)
                .recentAttendance(recentList)
                .biometricHistory(bioHistory)
                .build();
    }

    @Transactional(readOnly = true)
    public List<StudentShortageDto> getShortageStudents() {
        List<AttendanceSummary> shortageSummaries = summaryRepository.findShortageSummaries();
        List<StudentShortageDto> list = new ArrayList<>();

        for (AttendanceSummary s : shortageSummaries) {
            AttendanceShortageCalculation calc = calculationService.calculateShortage(
                    s.getPresentClasses(), s.getLateClasses(), s.getTotalClasses()
            );
            String roll = s.getStudent() != null ? s.getStudent().getRollNo() : "";
            String name = s.getStudent() != null ? s.getStudent().getName() : "";
            String reg = s.getStudent() != null ? s.getStudent().getUniversityRegNo() : "";
            Long stId = s.getStudent() != null ? s.getStudent().getId() : null;
            String code = s.getSubject() != null ? s.getSubject().getCourseCode() : "";
            String subName = s.getSubject() != null ? s.getSubject().getSubjectName() : "";

            list.add(StudentShortageDto.builder()
                    .studentId(stId)
                    .rollNo(roll)
                    .name(name)
                    .universityRegNo(reg)
                    .subjectCode(code)
                    .subjectName(subName)
                    .totalClasses(s.getTotalClasses())
                    .attendedClasses(s.getPresentClasses() + s.getLateClasses())
                    .attendancePercentage(s.getAttendancePercentage())
                    .classesNeededFor75(calc.getClassesNeededFor75())
                    .alertMessage(calc.getActionableAdvice())
                    .build());
        }
        return list;
    }

    public StudentDto mapToDto(Student s) {
        List<AttendanceSummary> summaries = summaryRepository.findByStudentId(s.getId());
        int totalConducted = 0;
        int totalAttended = 0;
        for (AttendanceSummary sum : summaries) {
            totalConducted += sum.getTotalClasses();
            totalAttended += (sum.getPresentClasses() + sum.getLateClasses());
        }
        BigDecimal overall = calculationService.calculatePercentage(totalAttended, 0, 0, totalConducted, BigDecimal.ONE);
        EligibilityStatus eligibility = calculationService.determineEligibility(overall);

        Long deptId = s.getDepartment() != null ? s.getDepartment().getId() : null;
        String deptName = s.getDepartment() != null ? s.getDepartment().getName() : "";
        Long brId = s.getBranch() != null ? s.getBranch().getId() : null;
        String brName = s.getBranch() != null ? s.getBranch().getName() : "";
        Long btId = s.getBatch() != null ? s.getBatch().getId() : null;
        String btName = s.getBatch() != null ? s.getBatch().getName() : "";
        Long semId = s.getSemester() != null ? s.getSemester().getId() : null;
        String semName = s.getSemester() != null ? s.getSemester().getName() : "";
        Long secId = s.getSection() != null ? s.getSection().getId() : null;
        String secName = s.getSection() != null ? s.getSection().getName() : "";
        Long uId = s.getUser() != null ? s.getUser().getId() : null;

        return StudentDto.builder()
                .id(s.getId())
                .userId(uId)
                .universityRegNo(s.getUniversityRegNo())
                .rollNo(s.getRollNo())
                .name(s.getName())
                .fatherName(s.getFatherName())
                .motherName(s.getMotherName())
                .email(s.getEmail())
                .mobile(s.getMobile())
                .dateOfBirth(s.getDateOfBirth())
                .gender(s.getGender())
                .departmentId(deptId)
                .departmentName(deptName)
                .branchId(brId)
                .branchName(brName)
                .batchId(btId)
                .batchName(btName)
                .semesterId(semId)
                .semesterName(semName)
                .sectionId(secId)
                .sectionName(secName)
                .admissionYear(s.getAdmissionYear())
                .profilePhoto(s.getProfilePhoto())
                .biometricId(s.getBiometricId())
                .status(s.getStatus())
                .overallAttendancePercentage(overall)
                .eligibilityStatus(eligibility.name())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
