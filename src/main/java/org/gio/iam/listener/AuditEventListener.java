package org.gio.iam.listener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gio.iam.audit.AuditLog;
import org.gio.iam.event.AuditEvent;
import org.gio.iam.repository.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Slf4j
@RequiredArgsConstructor

public class AuditEventListener {

    private  final AuditLogRepository auditLogRepository;

    @Async
    @EventListener
    public void handleAsyncEvent(AuditEvent event){
        log.info("Async processing audit log for User: [{}] Action: [{}]", event.getUsername(),event.getAction());

        AuditLog logEntity = new AuditLog();
        logEntity.setUsername(event.getUsername());
        logEntity.setAction(event.getAction());
        logEntity.setResource(event.getResource());
        logEntity.setSuccess(event.isSuccess());
        logEntity.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(logEntity);
    }
}
