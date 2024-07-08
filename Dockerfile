# Command: docker build -t school-management-system-spring:1.0.0 .

# 1ª Step -> Build stage


# Use an official Maven image as a build stage
FROM maven:3.9.8-amazoncorretto-21 AS build

# Set or create the build directory
WORKDIR /build

# Copy pom.xml file to the build directory
COPY pom.xml .

# Download all dependencies and plugins for the build process
RUN mvn dependency:go-offline

# Copy the source code to the build directory
COPY src ./src

# Clean and build the application without running the tests
RUN mvn clean package -DskipTests


# Runtime stage


# Use an official OpenJDK image as a runtime stage
FROM amazoncorretto:21

# set the app arguments
ARG PROFILE=prod
ARG APP_VERSION=1.0.0

# Set or create the app directory
WORKDIR /app

# Copy the JAR file from the build stage to the app directory
COPY --from=build /build/target/School-Management-System-Spring-1.0.0.jar /app/

# Expose the port that the application will listen on
EXPOSE 8080

ENV POSTGRES_URL=jdbc:postgresql://postgres-school-management:5432/school_management_database
ENV POSTGRES_USERNAME=missing_postgres_username
ENV POSTGRES_PASSWORD=missing_postgres_password
ENV JAR_VERSION=${APP_VERSION}
ENV SPRING_PROFILES_ACTIVE=${PROFILE}
ENV JWT_SECRET=missing_jwt_secret
ENV ADMIN_EMAIL=missing_admin_email
ENV ADMIN_PASSWORD=missing_admin_password
ENV MAIL_USERNAME=missing_mail_username
ENV MAIL_PASSWORD=missing_mail_password


# Run the JAR file
CMD java -jar -Dspring.datasource.url=${POSTGRES_URL} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} School-Management-System-Spring-${JAR_VERSION}.jar