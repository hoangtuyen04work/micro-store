# Build stage
FROM maven:3.9.9-amazoncorretto-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Run stage
FROM amazoncorretto:21.0.5-al2023

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
