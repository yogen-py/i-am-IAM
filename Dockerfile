# --- Stage 1: Build (The Factory) ---
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
# Copy only pom.xml first to cache dependencies (Docker Layer Optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
# Skip tests here because GitHub Actions handles the verification phase separately
RUN mvn clean package -DskipTests

# --- Stage 2: Run (The Secure Runtime) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# SECURITY: Create a non-root group and user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the JAR from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# SECURITY: Switch to non-root user
USER spring:spring

# Expose the standard port
EXPOSE 8080

# Hardened Entrypoint with 'docker' profile activated
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
