package com.smartattend.service;

import com.smartattend.dto.AcademicDtoModels.*;
import com.smartattend.dto.TimetableDtoModels.*;
import com.smartattend.entity.*;
import com.smartattend.exception.ResourceNotFoundException;
import com.smartattend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcademicService {

    private static final Logger log = LoggerFactory.getLogger(AcademicService.class);

    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;
    private final BatchRepository batchRepository;
    private final SemesterRepository semesterRepository;
    private final SectionRepository sectionRepository;
    private final TimetableRepository timetableRepository;
    private final AcademicCalendarRepository calendarRepository;
    private final AttendanceRuleRepository attendanceRuleRepository;
    private final AttendanceMarkRuleRepository attendanceMarkRuleRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final AuditService auditService;

    public AcademicService(DepartmentRepository departmentRepository,
                           BranchRepository branchRepository,
                           BatchRepository batchRepository,
                           SemesterRepository semesterRepository,
                           SectionRepository sectionRepository,
                           TimetableRepository timetableRepository,
                           AcademicCalendarRepository calendarRepository,
                           AttendanceRuleRepository attendanceRuleRepository,
                           AttendanceMarkRuleRepository attendanceMarkRuleRepository,
                           SubjectRepository subjectRepository,
                           FacultyRepository facultyRepository,
                           AuditService auditService) {
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
        this.batchRepository = batchRepository;
        this.semesterRepository = semesterRepository;
        this.sectionRepository = sectionRepository;
        this.timetableRepository = timetableRepository;
        this.calendarRepository = calendarRepository;
        this.attendanceRuleRepository = attendanceRuleRepository;
        this.attendanceMarkRuleRepository = attendanceMarkRuleRepository;
        this.subjectRepository = subjectRepository;
        this.facultyRepository = facultyRepository;
        this.auditService = auditService;
    }

    // Departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department createDepartment(Department department) {
        department.setIsActive(true);
        Department saved = departmentRepository.save(department);
        auditService.log("CREATE_DEPARTMENT", "Department", saved.getId().toString(), null, saved.getCode(), null, "Created department: " + saved.getName());
        return saved;
    }

    // Branches
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    // Batches
    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    // Semesters
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    // Sections
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public List<Section> getSectionsBySemester(Long semesterId) {
        return sectionRepository.findBySemesterId(semesterId);
    }

    // Timetable
    @Transactional(readOnly = true)
    public List<TimetableDto> getTimetableBySection(Long sectionId) {
        return timetableRepository.findBySectionIdAndIsActiveTrue(sectionId).stream()
                .map(this::mapToTimetableDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableDto> getTimetableByFaculty(Long facultyId) {
        return timetableRepository.findByFacultyIdAndIsActiveTrue(facultyId).stream()
                .map(this::mapToTimetableDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TimetableDto createTimetableEntry(TimetableCreateRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));
        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found: " + request.getFacultyId()));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found: " + request.getSectionId()));
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester not found: " + request.getSemesterId()));

        Timetable timetable = new Timetable();
        timetable.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        timetable.setStartTime(request.getStartTime());
        timetable.setEndTime(request.getEndTime());
        timetable.setPeriodNumber(request.getPeriodNumber());
        timetable.setSubject(subject);
        timetable.setFaculty(faculty);
        timetable.setRoomNo(request.getRoomNo());
        timetable.setSection(section);
        timetable.setSemester(semester);
        timetable.setAcademicSession(request.getAcademicSession());
        timetable.setIsActive(true);

        timetable = timetableRepository.save(timetable);
        return mapToTimetableDto(timetable);
    }

    @Transactional(readOnly = true)
    public Optional<TimetableDto> getCurrentActiveSlotForFaculty(Long facultyId) {
        DayOfWeek dow = LocalDate.now().getDayOfWeek();
        LocalTime now = LocalTime.now();
        return timetableRepository.findCurrentSlotForFaculty(dow.name(), now, facultyId)
                .map(this::mapToTimetableDto);
    }

    // Academic Calendar
    @Transactional(readOnly = true)
    public List<AcademicCalendarDto> getCalendarEvents(String session) {
        List<AcademicCalendar> events = session != null ?
                calendarRepository.findByAcademicSessionOrderByStartDateAsc(session) :
                calendarRepository.findByIsActiveTrueOrderByStartDateAsc();

        return events.stream().map(e -> AcademicCalendarDto.builder()
                .id(e.getId())
                .eventTitle(e.getEventTitle())
                .eventType(e.getEventType())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .totalDays(e.getTotalDays())
                .dayName(e.getDayName())
                .description(e.getDescription())
                .academicSession(e.getAcademicSession())
                .isActive(e.getIsActive())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    public AcademicCalendarDto createCalendarEvent(AcademicCalendarDto dto) {
        AcademicCalendar event = new AcademicCalendar();
        event.setEventTitle(dto.getEventTitle());
        event.setEventType(dto.getEventType() != null ? dto.getEventType() : com.smartattend.enums.EventType.HOLIDAY);
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setTotalDays(dto.getTotalDays() != null ? dto.getTotalDays() : 1);
        event.setDayName(dto.getDayName());
        event.setDescription(dto.getDescription());
        event.setAcademicSession(dto.getAcademicSession() != null ? dto.getAcademicSession() : "2026–2030");
        event.setIsActive(true);

        event = calendarRepository.save(event);
        dto.setId(event.getId());
        return dto;
    }

    // Rules
    @Transactional(readOnly = true)
    public AttendanceRuleDto getActiveAttendanceRule() {
        AttendanceRule rule = attendanceRuleRepository.findByIsActiveTrue()
                .orElseGet(() -> {
                    AttendanceRule r = new AttendanceRule();
                    r.setRuleName("BEU 75% Rule");
                    r.setMinPercentageRequired(BigDecimal.valueOf(75.0));
                    r.setCondonationPercentageAllowed(BigDecimal.valueOf(15.0));
                    r.setLateAsPresentWeight(BigDecimal.ONE);
                    r.setIsActive(true);
                    r.setDescription("Default BEU Attendance Rule");
                    return r;
                });

        return AttendanceRuleDto.builder()
                .id(rule.getId())
                .ruleName(rule.getRuleName())
                .minPercentageRequired(rule.getMinPercentageRequired())
                .condonationPercentageAllowed(rule.getCondonationPercentageAllowed())
                .lateAsPresentWeight(rule.getLateAsPresentWeight())
                .isActive(rule.getIsActive())
                .description(rule.getDescription())
                .updatedAt(rule.getUpdatedAt())
                .build();
    }

    @Transactional
    public AttendanceRuleDto updateAttendanceRule(AttendanceRuleDto dto) {
        AttendanceRule rule = attendanceRuleRepository.findByIsActiveTrue()
                .orElseGet(() -> {
                    AttendanceRule r = new AttendanceRule();
                    r.setRuleName("Default Rule");
                    return r;
                });

        rule.setMinPercentageRequired(dto.getMinPercentageRequired());
        rule.setCondonationPercentageAllowed(dto.getCondonationPercentageAllowed());
        rule.setLateAsPresentWeight(dto.getLateAsPresentWeight());
        if (dto.getDescription() != null) rule.setDescription(dto.getDescription());
        rule = attendanceRuleRepository.save(rule);

        auditService.log("UPDATE_RULE", "AttendanceRule", rule.getId() != null ? rule.getId().toString() : "1",
                null, rule.getMinPercentageRequired().toString(), null, "Updated attendance threshold rules");

        return getActiveAttendanceRule();
    }

    @Transactional(readOnly = true)
    public List<AttendanceMarkRuleDto> getAttendanceMarkRules() {
        return attendanceMarkRuleRepository.findByIsActiveTrueOrderByMinPercentageAsc().stream()
                .map(r -> AttendanceMarkRuleDto.builder()
                        .id(r.getId())
                        .minPercentage(r.getMinPercentage())
                        .maxPercentage(r.getMaxPercentage())
                        .marksAwarded(r.getMarksAwarded())
                        .ruleDescription(r.getRuleDescription())
                        .isActive(r.getIsActive())
                        .build()).collect(Collectors.toList());
    }

    private TimetableDto mapToTimetableDto(Timetable t) {
        Long subId = t.getSubject() != null ? t.getSubject().getId() : null;
        String code = t.getSubject() != null ? t.getSubject().getCourseCode() : "";
        String subName = t.getSubject() != null ? t.getSubject().getSubjectName() : "";

        Long fId = t.getFaculty() != null ? t.getFaculty().getId() : null;
        String fName = t.getFaculty() != null ? t.getFaculty().getName() : "";

        Long secId = t.getSection() != null ? t.getSection().getId() : null;
        String secName = t.getSection() != null ? t.getSection().getName() : "";

        Long semId = t.getSemester() != null ? t.getSemester().getId() : null;
        String semName = t.getSemester() != null ? t.getSemester().getName() : "";

        return TimetableDto.builder()
                .id(t.getId())
                .dayOfWeek(t.getDayOfWeek())
                .startTime(t.getStartTime())
                .endTime(t.getEndTime())
                .periodNumber(t.getPeriodNumber())
                .subjectId(subId)
                .subjectCode(code)
                .subjectName(subName)
                .facultyId(fId)
                .facultyName(fName)
                .roomNo(t.getRoomNo())
                .sectionId(secId)
                .sectionName(secName)
                .semesterId(semId)
                .semesterName(semName)
                .academicSession(t.getAcademicSession())
                .isActive(t.getIsActive())
                .build();
    }
}
