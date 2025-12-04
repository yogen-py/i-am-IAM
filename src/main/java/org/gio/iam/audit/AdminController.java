package org.gio.iam.audit;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // This is the Gatekeeper. Only users with ROLE_ADMIN can pass.
    @GetMapping("/audit-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAuditLogs(Authentication authentication) {

        // Return dummy data to prove it works
        return List.of(
                new AuditLog(
                        UUID.randomUUID(),
                        authentication.getName(), // This comes from 'preferred_username' in our Converter
                        "VIEW_LOGS",
                        LocalDateTime.now(),
                        "User accessed the audit log endpoint"
                )
        );
    }
}