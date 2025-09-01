# Build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Install protobuf compiler and dependencies
RUN apt-get update && apt-get install -y \
    protobuf-compiler \
    && rm -rf /var/lib/apt/lists/*

# Copy parent pom first for dependency resolution
COPY pom.xml ./
COPY apps/pom.xml ./apps/
COPY common ./common/

# Copy all app modules (needed for Maven build to work)
COPY apps/api-gateway ./apps/api-gateway/
COPY apps/auth-service ./apps/auth-service/
# COPY apps/user-service ./apps/user-service/
COPY apps/patient-record-service ./apps/patient-record-service/
COPY apps/triage-orchestrator-service ./apps/triage-orchestrator-service/

# COPY . .

# Build the application
RUN mvn clean package -pl apps/api-gateway -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built JAR
COPY --from=build /app/apps/api-gateway/target/*.jar app.jar

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown appuser:appuser app.jar
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
