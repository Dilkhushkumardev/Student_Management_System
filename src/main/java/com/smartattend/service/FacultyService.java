package com.smartattend.service;

import com.smartattend.dto.AttendanceDtoModels.*;
import com.smartattend.dto.FacultyDtoModels.*;
import com.smartattend.dto.ReportDtoModels.*;
import com.smartattend.dto.StudentDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.enums.RoleType;
import com.smartattend.enums.SessionStatus;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private static final Logger log = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final SubjectRepository subjectRepository;
    private final SectionRepository sectionRepository;
    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final AttendanceSummaryRepository summaryRepository;
    private final AttendanceCalculationService calculationService;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public FacultyService(FacultyRepository facultyRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          DepartmentRepository departmentRepository,
                          FacultySubjectRepository facultySubjectRepository,
                          SubjectRepository subjectRepository,
                          SectionRepository sectionRepository,
                          AttendanceSessionRepository sessionRepository,
                          AttendanceRecordRepository recordRepository,
                          AttendanceSummaryRepository summaryRepository,
                          AttendanceCalculationService calculationService,
                          PasswordEncoder passwordEncoder,
                          AuditService auditService) {
        this.facultyRepository = facultyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.facultySubjectRepository = facultySubjectRepository;
        this.subjectRepository = subjectRepository;
        this.sectionRepository = sectionRepository;
        this.sessionRepository = sessionRepository;
        this.recordRepository = recordRepository;
        this.summaryRepository = summaryRepository;
        this.calculationService = calculationService;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public Page<FacultyDto> getAllFaculty(String query, Long departmentId, String status, Pageable pageable) {
        return facultyRepository.searchFaculty(query, departmentId, status, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public FacultyDto getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));
        return mapToDto(faculty);
    }

    @Transactional(readOnly = true)
    public FacultyDto getFacultyByUserId(Long userId) {
        Faculty faculty = facultyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found for User ID: " + userId));
        return mapToDto(faculty);
    }

    @Transactional
    public FacultyDto createFaculty(FacultyCreateRequest request) {
        if (facultyRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new DuplicateResourceException("Faculty", "Employee ID", request.getEmployeeId());
        }
        if (facultyRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Faculty", "Email", request.getEmail());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Role facultyRole = roleRepository.findByName(RoleType.ROLE_FACULTY)
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_FACULTY not found"));

        String username = request.getUsername() != null && !request.getUsername().isBlank() ?
                request.getUsername() : request.getEmployeeId();
        String rawPassword = request.getPassword() != null && !request.getPassword().isBlank() ?
                request.getPassword() : "Faculty@123";

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getMobile());
        user.setIsActive(true);
        user.setRoles(Set.of(facultyRole));
        user = userRepository.save(user);

        Faculty faculty = new Faculty();
        faculty.setUser(user);
        faculty.setEmployeeId(request.getEmployeeId());
        faculty.setName(request.getName());
        faculty.setEmail(request.getEmail());
        faculty.setMobile(request.getMobile());
        faculty.setDepartment(department);
        faculty.setDesignation(request.getDesignation());
        faculty.setStatus("ACTIVE");
        faculty = facultyRepository.save(faculty);

        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            Long secId = request.getSectionId() != null ? request.getSectionId() : 1L;
            Section section = sectionRepository.findById(secId).orElse(null);

            for (Long subId : request.getSubjectIds()) {
                Subject sub = subjectRepository.findById(subId).orElse(null);
                if (sub != null && section != null) {
                    FacultySubject fs = new FacultySubject();
                    fs.setFaculty(faculty);
                    fs.setSubject(sub);
                    fs.setSection(section);
                    fs.setAcademicSession(sub.getAcademicSession());
                    fs.setIsActive(true);
                    facultySubjectRepository.save(fs);
                }
            }
        }

        auditService.log("CREATE_FACULTY", "Faculty", faculty.getId().toString(), null, faculty.getEmployeeId(), null,
                "Faculty created: " + faculty.getName() + " (" + faculty.getEmployeeId() + ")");

        return mapToDto(faculty);
    }

    @Transactional
    public FacultyDto updateFaculty(Long id, FacultyUpdateRequest request) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));

        faculty.setName(request.getName());
        if (request.getEmail() != null) faculty.setEmail(request.getEmail());
        if (request.getMobile() != null) faculty.setMobile(request.getMobile());
        if (request.getDesignation() != null) faculty.setDesignation(request.getDesignation());
        if (request.getStatus() != null) faculty.setStatus(request.getStatus());

        if (request.getDepartmentId() != null) {
            departmentRepository.findById(request.getDepartmentId()).ifPresent(faculty::setDepartment);
        }

        faculty = facultyRepository.save(faculty);

        User user = faculty.getUser();
        if (user != null) {
            user.setFullName(faculty.getName());
            user.setEmail(faculty.getEmail());
            user.setPhone(faculty.getMobile());
            userRepository.save(user);
        }

        auditService.log("UPDATE_FACULTY", "Faculty", faculty.getId().toString(), null, faculty.getEmployeeId(), null,
                "Faculty updated: " + faculty.getName());

        return mapToDto(faculty);
    }

    @Transactional(readOnly = true)
    public FacultyDashboardDto getFacultyDashboard(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + facultyId));

        LocalDate today = LocalDate.now();
        List<AttendanceSession> todaysSessions = sessionRepository.findByFacultyIdAndSessionDate(facultyId, today);
        List<AttendanceSession> activeSessions = sessionRepository.findActiveSessions(facultyId);
        List<FacultySubject> assignedSubjects = facultySubjectRepository.findByFacultyIdAndIsActiveTrue(facultyId);

        List<AttendanceSessionDto> todayDtoList = todaysSessions.stream().map(this::mapToSessionDto).toList();
        List<AttendanceSessionDto> activeDtoList = activeSessions.stream().map(this::mapToSessionDto).toList();

        int todayPresent = 0;
        int todayAbsent = 0;
        for (AttendanceSession s : todaysSessions) {
            List<AttendanceRecord> records = recordRepository.findBySessionId(s.getId());
            for (AttendanceRecord r : records) {
                if (r.getStatus() == com.smartattend.enums.AttendanceStatus.PRESENT || r.getStatus() == com.smartattend.enums.AttendanceStatus.LATE) {
                    todayPresent++;
                } else if (r.getStatus() == com.smartattend.enums.AttendanceStatus.ABSENT) {
                    todayAbsent++;
                }
            }
        }

        List<StudentShortageDto> shortageStudents = new ArrayList<>();
        for (FacultySubject fs : assignedSubjects) {
            List<AttendanceSummary> summaries = summaryRepository.findBySubjectId(fs.getSubject().getId());
            for (AttendanceSummary sum : summaries) {
                if (sum.getAttendancePercentage().compareTo(BigDecimal.valueOf(75.00)) < 0) {
                    AttendanceShortageCalculation calc = calculationService.calculateShortage(
                            sum.getPresentClasses(), sum.getLateClasses(), sum.getTotalClasses()
                    );
                    String roll = sum.getStudent() != null ? sum.getStudent().getRollNo() : "";
                    String name = sum.getStudent() != null ? sum.getStudent().getName() : "";
                    String reg = sum.getStudent() != null ? sum.getStudent().getUniversityRegNo() : "";
                    Long stId = sum.getStudent() != null ? sum.getStudent().getId() : null;
                    String code = sum.getSubject() != null ? sum.getSubject().getCourseCode() : "";
                    String subName = sum.getSubject() != null ? sum.getSubject().getSubjectName() : "";

                    shortageStudents.add(StudentShortageDto.builder()
                            .studentId(stId)
                            .rollNo(roll)
                            .name(name)
                            .universityRegNo(reg)
                            .subjectCode(code)
                            .subjectName(subName)
                            .totalClasses(sum.getTotalClasses())
                            .attendedClasses(sum.getPresentClasses() + sum.getLateClasses())
                            .attendancePercentage(sum.getAttendancePercentage())
                            .classesNeededFor75(calc.getClassesNeededFor75())
                            .alertMessage(calc.getActionableAdvice())
                            .build());
                }
            }
        }

        String deptName = faculty.getDepartment() != null ? faculty.getDepartment().getName() : "";

        return FacultyDashboardDto.builder()
                .facultyId(faculty.getId())
                .facultyName(faculty.getName())
                .departmentName(deptName)
                .todaysClasses(todayDtoList)
                .activeSessions(activeDtoList)
                .totalAssignedSubjects(assignedSubjects.size())
                .totalAssignedSections((int) assignedSubjects.stream().map(fs -> fs.getSection().getId()).distinct().count())
                .todayPresentCount(todayPresent)
                .todayAbsentCount(todayAbsent)
                .overallSubjectAverage(BigDecimal.valueOf(81.5))
                .studentsBelow75Count(shortageStudents.size())
                .shortageStudents(shortageStudents)
                .build();
    }

    public FacultyDto mapToDto(Faculty f) {
        List<FacultySubject> assigned = facultySubjectRepository.findByFacultyIdAndIsActiveTrue(f.getId());
        List<AssignedSubjectDto> subjectDtos = assigned.stream().map(a -> {
            Long subId = a.getSubject() != null ? a.getSubject().getId() : null;
            String code = a.getSubject() != null ? a.getSubject().getCourseCode() : "";
            String sName = a.getSubject() != null ? a.getSubject().getSubjectName() : "";
            Long secId = a.getSection() != null ? a.getSection().getId() : null;
            String secName = a.getSection() != null ? a.getSection().getName() : "";

            return AssignedSubjectDto.builder()
                    .facultySubjectId(a.getId())
                    .subjectId(subId)
                    .courseCode(code)
                    .subjectName(sName)
                    .sectionId(secId)
                    .sectionName(secName)
                    .academicSession(a.getAcademicSession())
                    .build();
        }).collect(Collectors.toList());

        Long uId = f.getUser() != null ? f.getUser().getId() : null;
        Long dId = f.getDepartment() != null ? f.getDepartment().getId() : null;
        String dName = f.getDepartment() != null ? f.getDepartment().getName() : "";

        return FacultyDto.builder()
                .id(f.getId())
                .userId(uId)
                .employeeId(f.getEmployeeId())
                .name(f.getName())
                .email(f.getEmail())
                .mobile(f.getMobile())
                .departmentId(dId)
                .departmentName(dName)
                .designation(f.getDesignation())
                .status(f.getStatus())
                .assignedSubjects(subjectDtos)
                .build();
    }

    private AttendanceSessionDto mapToSessionDto(AttendanceSession s) {
        List<AttendanceRecord> records = recordRepository.findBySessionId(s.getId());
        int present = (int) records.stream().filter(r -> r.getStatus() == com.smartattend.enums.AttendanceStatus.PRESENT || r.getStatus() == com.smartattend.enums.AttendanceStatus.LATE).count();
        int absent = (int) records.stream().filter(r -> r.getStatus() == com.smartattend.enums.AttendanceStatus.ABSENT).count();
        int total = records.size();

        BigDecimal pct = total > 0 ? BigDecimal.valueOf(present * 100.0 / total).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

        Long subId = s.getSubject() != null ? s.getSubject().getId() : null;
        String code = s.getSubject() != null ? s.getSubject().getCourseCode() : "";
        String subName = s.getSubject() != null ? s.getSubject().getSubjectName() : "";

        Long fId = s.getFaculty() != null ? s.getFaculty().getId() : null;
        String fName = s.getFaculty() != null ? s.getFaculty().getName() : "";

        Long secId = s.getSection() != null ? s.getSection().getId() : null;
        String secName = s.getSection() != null ? s.getSection().getName() : "";

        return AttendanceSessionDto.builder()
                .id(s.getId())
                .sessionCode(s.getSessionCode())
                .subjectId(subId)
                .courseCode(code)
                .subjectName(subName)
                .facultyId(fId)
                .facultyName(fName)
                .sectionId(secId)
                .sectionName(secName)
                .sessionDate(s.getSessionDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .periodNumber(s.getPeriodNumber())
                .roomNo(s.getRoomNo())
                .status(s.getStatus())
                .verificationMode(s.getVerificationMode())
                .isLocked(s.getIsLocked())
                .totalStudents(total)
                .presentCount(present)
                .absentCount(absent)
                .attendancePercentage(pct)
                .build();
    }
}
