package com.smartattend.service;

import com.smartattend.dto.AcademicDtoModels.AuditLogDto;
import com.smartattend.entity.AuditLog;
import com.smartattend.repository.AuditLogRepository;
import com.smartattend.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action, String entityName, String entityId, String oldValue, String newValue, String ipAddress, String details) {
        String username = SecurityUtils.getCurrentUsername().orElse("SYSTEM");
        Long userId = SecurityUtils.getCurrentUserId().orElse(null);
        String role = SecurityUtils.getCurrentUser()
                .map(u -> u.getAuthorities().toString())
                .orElse("ROLE_SYSTEM");

        AuditLog logEntry = new AuditLog();
        logEntry.setUserId(userId);
        logEntry.setUsername(username);
        logEntry.setUserRole(role);
        logEntry.setAction(action);
        logEntry.setEntityName(entityName);
        logEntry.setEntityId(entityId);
        logEntry.setOldValue(oldValue);
        logEntry.setNewValue(newValue);
        logEntry.setIpAddress(ipAddress != null ? ipAddress : "127.0.0.1");
        logEntry.setDetails(details);
        logEntry.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(logEntry);
        log.info("AUDIT LOG: [{} - {}] by {} ({}) on {} #{}", action, entityName, username, role, entityName, entityId);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByTimestampDesc(pageable)
                .map(this::mapToDto);
    }

    private AuditLogDto mapToDto(AuditLog l) {
        return AuditLogDto.builder()
                .id(l.getId())
                .userId(l.getUserId())
                .username(l.getUsername())
                .userRole(l.getUserRole())
                .action(l.getAction())
                .entityName(l.getEntityName())
                .entityId(l.getEntityId())
                .oldValue(l.getOldValue())
                .newValue(l.getNewValue())
                .ipAddress(l.getIpAddress())
                .details(l.getDetails())
                .timestamp(l.getTimestamp())
                .build();
    }
}
