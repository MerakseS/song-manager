FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} discovery-service.jar
ENTRYPOINT ["java", "-jar", "discovery-service.jar"]