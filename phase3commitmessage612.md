Feature: Stabilize and Complete Phase 3 Security Core

Successfully resolved integration issues to fully stabilize the core security features required for production hardening. Rate Limiting is temporarily disabled to ensure focus on core security.

### Key Achievements:
* **Token Revocation (Blacklist) Implemented:** Resolved the `UnknownHostException` and confirmed the `TokenBlacklistService` now successfully writes token IDs (`jti`) to Redis and blocks subsequent access via the `JwtRevocationFilter` (Test A Passed).
* **Asynchronous Audit Logging Verified:** Confirmed the `ApplicationEventPublisher` and `@Async` listener are correctly decoupling DB write operations, proving the performance goal is met.
* **Networking & Configuration Fixed:** Resolved the Host vs. Docker networking conflict by correctly setting `spring.data.redis.host=localhost` in `application.properties`.
* **Code Robustness:** Updated `AuthController` with defensive null checks to prevent `NullPointerException` warnings during token expiration extraction.

### Known Issue:
* **Distributed Rate Limiting:** Temporarily disabled (`bucket4j.enabled=false`) to prevent further interference due to persistent silent failure in cache initialization. To be addressed in Phase 4.
