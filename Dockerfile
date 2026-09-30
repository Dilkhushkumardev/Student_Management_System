# ==============================================================================
# SmartAttend - Docker Multi-Stage Build
# Java 21 LTS + Spring Boot 3.3.4
# ==============================================================================

FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy Maven Wrapper & POM
COPY pom.xml .
COPY src ./src

# Build JAR package (skipping tests for fast container generation)
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Stage 2: Runtime Image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root system user
RUN groupadd -r smartattend && useradd -r -g smartattend smartattend

# Copy compiled JAR
COPY --from=build /app/target/smart-attend-1.0.0.jar app.jar

# Configuration environment variables
ENV SPRING_PROFILES_ACTIVE=mysql \
    DB_HOST=mysql \
    DB_PORT=3306 \
    DB_NAME=smartattend_db \
    DB_USERNAME=smartattend \
    DB_PASSWORD=smartattend_secret \
    JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970 \
    PORT=8080

EXPOSE 8080

USER smartattend

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
