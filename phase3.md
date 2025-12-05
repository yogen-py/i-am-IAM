Feature: Complete Phase 3 - Production Hardening (Redis, Security, Async)

Completed the implementation of critical enterprise hardening features, leveraging Redis for distributed security and resilience, and integrating Spring Events for performance optimization.

### Infrastructure & Configuration
* **Redis Integration:** Successfully added the Redis service to `docker-compose.yaml` and configured `RedisConfig.java` for connection factory and `RedisTemplate`.
* **Dependencies:** Added necessary dependencies for Spring Data Redis and Bucket4j to `pom.xml`.
* **Environment Alignment:** Updated `application.properties` with Bucket4j configuration.

### Security: Token Revocation (Blocklist)
* Implemented stateless token revocation using a Redis blocklist.
* **Service:** Created `TokenBlacklistService` to store JWT IDs (`jti`) in Redis with the token's remaining TTL.
* **Endpoint:** Added `POST /auth/logout` to trigger the blacklist process.
* **Defense:** Created and wired `JwtRevocationFilter` into `SecurityConfig.java`, ensuring it runs **after** signature validation to block revoked tokens instantly.

### Resilience: Distributed Rate Limiting
* Integrated `bucket4j-spring-boot-starter` for distributed traffic control.
* **Configuration:** Configured the rate limit to 10 requests per minute across all instances.
* **Keying:** Ensured the limit is tracked per user by setting the `cache-key` to `getSubject()` (User ID from JWT), leveraging Redis for shared state.

### Performance: Asynchronous Audit Logging
* Refactored logging to eliminate I/O blocking in the response thread.
* **Event System:** Created `AuditEvent`, `AuditEventListener`, and enabled `@EnableAsync` on the main application class.
* **Controller Refactor:** `AdminController` was updated to fetch real data via `AuditLogRepository.findAll()` and now publishes an `AuditEvent` instead of logging directly.

### Data Alignment & Stability
* **Schema Sync:** Updated the `V1__init_audit.sql` Flyway script to include new columns (`resource`, `success`) and rename `user_id` to `username`.
* **DB Reset:** Executed `docker compose down -v` to reset volumes, ensuring Flyway executed the new schema, syncing the database with the `AuditLog` Entity.
