# Enterprise IAM & IGA Microservice

**Status:** `Production-Ready` | **Engineered by:** Yogen Aralaguppi

**Stack:** Java 21, Spring Boot 3.3.5, Keycloak, PostgreSQL, Kubernetes, DevSecOps (Trivy)

---

## Project Overview

This is a high-security **Identity and Access Management (IAM)** and **Identity Governance (IGA)** microservice. It is designed to handle enterprise-grade authentication and authorization workflows using a **Zero-Trust** security model.

The system integrates with **Keycloak** for OIDC/OAuth2 identity management and implements a custom, asynchronous audit trail to ensure compliance with standards like **ISO/IEC 27001**.

###  Key Features

* **Centralized Identity:** Full delegation of AuthN/AuthZ to Keycloak.
* **Granular RBAC:** Custom JWT converters mapping Keycloak roles to Spring Security authorities.
* **Asynchronous Auditing:** High-performance, event-driven audit logging (Producer-Consumer pattern).
* **Self-Healing Infrastructure:** Kubernetes manifests with Liveness/Readiness probes.
* **DevSecOps Pipeline:** Automated "Policy-as-Code" via GitHub Actions, including **Trivy** vulnerability scanning and **SBOM** generation.

---

##  Architecture & System Design

The application follows a decoupled microservice architecture:

1. **Security Layer:** Spring Security 6 handles JWT validation and method-level security (`@PreAuthorize`).
2. **Audit Engine:** An internal event-driven system that captures administrative actions without slowing down the main request thread.
3. **Persistence:** PostgreSQL stores audit trails and system metadata.
4. **DevSecOps:** A multi-stage Docker build reduces the attack surface by running the application as a **non-root user**.

---

##  Getting Started

### Prerequisites

* **Java 21**
* **Maven 3.9+**
* **Docker & Docker Compose**
* **Kubernetes** (Minikube or Docker Desktop K8s)

### 1. Local Infrastructure

Spin up the identity provider and database:

```bash
docker-compose up -d

```

### 2. Run the Application

```bash
mvn clean install
mvn spring-boot:run -Dspring-profiles.active=dev

```

### 3. Access Documentation

Once running, navigate to the **Swagger UI** to test the API:
`http://localhost:8081/swagger-ui.html`

---

## 🧪 Testing Strategy

The project utilizes a **Mock-First Strategy** to ensure high reliability and fast CI/CD execution.

* **MockMvc & JwtTestUtils:** Used to simulate valid/invalid JWTs to verify RBAC logic.
* **Mockito:** Used to verify that Audit Events are published correctly upon successful administrative actions.
* **Integration Tests:** Handled through the `mvn verify` lifecycle.

---

##  Deployment (Kubernetes)

The application is ready for cloud deployment via the `k8s/` manifests.

```bash
# Apply the deployment, service, and secrets
kubectl apply -f k8s/

```

**Key K8s Features implemented:**

* **Replicas:** 2 (High Availability).
* **Resources:** Explicit memory/CPU limits to prevent "noisy neighbor" issues.
* **Probes:** Health checks for automated self-healing.

---

##  DevSecOps & Security

This project implements a **Shift-Left Security** approach within GitHub Actions:

1. **Dependency Scanning:** Automates checking for vulnerable libraries.
2. **Container Scanning (Trivy):** Scans the built Docker image for OS and JRE vulnerabilities.
3. **Policy Enforcement:** The pipeline **fails** if `CRITICAL` vulnerabilities are detected.
4. **SBOM Generation:** Produces a Software Bill of Materials (SPDX) for supply chain transparency.

---

##  API Endpoints

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/admin/audit-logs` | `ROLE_ADMIN` | Fetches historical audit trails. |
| `GET` | `/api/public/health` | `Public` | Liveness/Readiness check. |
| `POST` | `/admin/users` | `ROLE_ADMIN` | Provision new user identities. |


