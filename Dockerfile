FROM maven:3.9.5-amazoncorretto-21 AS build

WORKDIR /workspace

# Copy the pom.xml and download dependencies, this improves Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the JAR without running tests
COPY src src
RUN mvn clean package -DskipTests

# Second stage: Run the application
FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /workspace/target/order-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]