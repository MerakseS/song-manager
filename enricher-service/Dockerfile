FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} enricher-service.jar
ENTRYPOINT ["java", "-jar", "enricher-service.jar"]