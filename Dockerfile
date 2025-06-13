#FROM eclipse-temurin:23-jdk
#
#WORKDIR /app
#
#COPY target/*.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Build the application
#FROM maven:3.9.6-eclipse-temurin-23 AS builder
#FROM eclipse-temurin:23-jdk
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Stage 2: Run the application
#FROM eclipse-temurin:23-jdk
#WORKDIR /app
#COPY --from=builder /app/target/*.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]