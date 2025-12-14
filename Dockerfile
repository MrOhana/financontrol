# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for security (GCP/Best Practice)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Configuration for Low Memory Environments (512MB RAM target)
# -Xmx300m: Cap Heap at 300MB
# -Xss512k: Reduce thread stack size
# -XX:+UseSerialGC: Low overhead Garbage Collector
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:+UseSerialGC"

# Expose port 8080 (Standard for Cloud Run)
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
