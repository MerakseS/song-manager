FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} file-api.jar
ENTRYPOINT ["java", "-jar", "file-api.jar"]