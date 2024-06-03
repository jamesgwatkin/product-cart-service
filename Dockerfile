FROM eclipse-temurin:22-jre-ubi9-minimal

# Create the directory for H2 database and set permissions
RUN mkdir -p /db
RUN chmod -R 777 /db

# Add a volume to persist the H2 database data
VOLUME /db

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=local

# Copy the jar file
COPY build/libs/*.jar app.jar

# Expose the port that the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]