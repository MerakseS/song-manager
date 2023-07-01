FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} auth-api.jar
ENTRYPOINT ["java", "-jar", "auth-api.jar"]