FROM maven:3.9-eclipse-temurin-17 AS builder
# Dockerfile — containerize our Spring Boot app
# Multi-stage build: Stage 1 builds the JAR, Stage 2 runs it.

# Stage 1: Build the application

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Download ALL dependencies first (cached in Docker layer).
# Next time you build, if only code changed (not pom.xml),
# Docker will use the cached layer and skip this step, speeding up builds.

COPY src ./src
RUN mvn package -DskipTests -B
# Build the JAR. -DskipTests = don't run tests during Docker build.
# The resulting JAR will be in target/ directory.
# Tests should run in CI pipeline, not during Docker build.

FROM eclipse-temurin:17-jre
# ONLY JRE needed to RUN (not full JDK). Alpine = tiny (~80MB vs ~400MB).

WORKDIR /app
# Copy ONLY the JAR from build stage. Build tools NOT included → tiny image!
COPY --from=builder /app/target/*.jar app.jar
# DON'T run as root! Create a non-root user for security.
# Create a group and user using Debian/Ubuntu syntax
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Set permissions for the jar file so appuser can run it
RUN chown appuser:appgroup app.jar

USER appuser

# port of the container ready to connect
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]