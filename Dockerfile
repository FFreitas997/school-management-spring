FROM amazoncorretto:21-alpine

LABEL authors="FFreitas"
LABEL description="This is a Dockerfile for a Spring Boot application"

WORKDIR /app

COPY target/School-Management-System-Spring-1.0.0.jar /app

EXPOSE 8080

CMD ["java", "-jar", "School-Management-System-Spring-1.0.0.jar"]