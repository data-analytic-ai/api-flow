# Dockerfile for APIFlow application

# Use the official OpenJDK 21 slim image as the base image
# This ensures we're using the latest long-term support version of Java (JDK 21)
FROM openjdk:21-jdk-slim

# Set the working directory inside the container to /app
WORKDIR /app

# Copy the built Spring Boot jar file from the target directory of the repository
# Make sure to build your project first so that the jar file exists in the target folder.
COPY target/api-flow-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080, which is the default port our Spring Boot application will run on.
EXPOSE 8080

# Define the command to run the application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
