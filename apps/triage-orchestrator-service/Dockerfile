# Build stage
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy all necessary files for Maven multi-module build
COPY pom.xml ./
COPY apps/pom.xml ./apps/
COPY common ./common/
COPY apps/api-gateway ./apps/api-gateway/
COPY apps/auth-service ./apps/auth-service/
COPY apps/patient-record-service ./apps/patient-record-service/
COPY apps/triage-orchestrator-service ./apps/triage-orchestrator-service/

# Build only the triage-orchestrator-service module
RUN mvn clean package -pl apps/triage-orchestrator-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built JAR
COPY --from=build /app/apps/triage-orchestrator-service/target/*.jar app.jar

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown appuser:appuser app.jar
USER appuser

# Expose ports (HTTP and gRPC)
EXPOSE 8083 9090

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8083/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
