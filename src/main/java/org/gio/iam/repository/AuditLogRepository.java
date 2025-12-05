package org.gio.iam.repository;

import org.gio.iam.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Standard CRUD methods are created automatically by Spring Data JPA
}