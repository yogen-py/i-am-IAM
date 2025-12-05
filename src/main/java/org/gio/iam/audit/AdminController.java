package org.gio.iam.audit;

import lombok.RequiredArgsConstructor;
import org.gio.iam.event.AuditEvent;
import org.gio.iam.repository.AuditLogRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor // Injects Repository and Publisher
public class AdminController {

    private final AuditLogRepository auditLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/audit-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAuditLogs(Authentication authentication) {

        // 1. ASYNC LOGGING: Fire and forget!
        // We log that the admin accessed this resource.
        eventPublisher.publishEvent(new AuditEvent(
                authentication.getName(), // User
                "VIEW_AUDIT_LOGS",        // Action
                "AdminController",        // Resource
                true                      // Success
        ));

        // 2. REAL DATA: Fetch from the DB
        return auditLogRepository.findAll();
    }
}