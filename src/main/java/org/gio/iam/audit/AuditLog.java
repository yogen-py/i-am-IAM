package org.gio.iam.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs") // This maps to the SQL table we will create next
@Data // Lombok: Generates Getters, Setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    private UUID id;
    private String userId;
    private String action;
    private LocalDateTime timestamp;
    private String details;
}