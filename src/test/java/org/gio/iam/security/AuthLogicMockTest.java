
package org.gio.iam.security;

import org.gio.iam.audit.AdminController;
import org.gio.iam.event.AuditEvent;
import org.gio.iam.repository.AuditLogRepository;
import org.gio.iam.service.TokenBlackListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import({ SecurityConfig.class, AuthLogicMockTest.EventPublisherTestConfig.class })
class AuthLogicMockTest {

    /**
     * Inner TestConfiguration that provides a @Primary mock of
     * ApplicationEventPublisher.
     * This is necessary because ApplicationEventPublisher is an infrastructure
     * interface
     * provided by the ApplicationContext itself, not a regular bean that @MockBean
     * can replace.
     */
    @TestConfiguration
    static class EventPublisherTestConfig {

        // Static field so we can access it in tests for verification
        static ApplicationEventPublisher mockPublisher = mock(ApplicationEventPublisher.class);

        @Bean
        @Primary
        public ApplicationEventPublisher testEventPublisher() {
            return mockPublisher;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    // --- DEPENDENCY MOCKS ---

    @MockBean
    private AuditLogRepository auditLogRepository; // For AdminController

    @MockBean
    private JwtDecoder jwtDecoder; // For SecurityConfig (JWT validation)

    @MockBean
    private TokenBlackListService tokenBlackListService; // Required by JwtRevocationFilter

    @Captor
    private ArgumentCaptor<AuditEvent> auditEventCaptor;

    // Reset the static mock before each test to avoid interaction leakage
    @BeforeEach
    void resetMocks() {
        Mockito.reset(EventPublisherTestConfig.mockPublisher);
    }

    // --- TEST CASES ---

    @Test
    @DisplayName("✅ RBAC: ROLE_ADMIN can access audit logs")
    void testAdminAccess_Success() throws Exception {
        mockMvc.perform(get("/admin/audit-logs")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("⛔ RBAC: ROLE_USER is denied access")
    void testUserAccess_Forbidden() throws Exception {
        mockMvc.perform(get("/admin/audit-logs")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("⚡ AUDIT: Accessing logs publishes an asynchronous event")
    void testAuditLog_EventPublishing() throws Exception {
        mockMvc.perform(get("/admin/audit-logs")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());

        // Verify using the static mock from our TestConfiguration
        verify(EventPublisherTestConfig.mockPublisher).publishEvent(any(AuditEvent.class));
    }

    @Test
    @DisplayName("⚡ AUDIT: Published event contains correct action details")
    void testAuditLog_EventContent() throws Exception {
        mockMvc.perform(get("/admin/audit-logs")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());

        // Capture and verify the actual event content
        verify(EventPublisherTestConfig.mockPublisher).publishEvent(auditEventCaptor.capture());

        AuditEvent capturedEvent = auditEventCaptor.getValue();
        assertThat(capturedEvent.getAction()).isEqualTo("VIEW_AUDIT_LOGS");
        assertThat(capturedEvent.getResource()).isEqualTo("AdminController");
        assertThat(capturedEvent.isSuccess()).isTrue();
    }
}
