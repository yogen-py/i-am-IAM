package org.gio.iam.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuditEvent {
    private String username;
    private String action;
    private String resource;
    private boolean success;
}
