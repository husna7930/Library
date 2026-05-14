# Use official Java runtime
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR file built by Maven (adjust name if different)
COPY target/library-system.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
