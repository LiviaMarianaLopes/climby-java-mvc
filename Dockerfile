FROM openjdk:21-jdk-slim AS builder

WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

COPY src ./src

RUN chmod +x ./gradlew

RUN ./gradlew build -x test --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

RUN groupadd -r appgroup && useradd --no-log-init -r -g appgroup appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
