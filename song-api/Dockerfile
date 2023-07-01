FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} song-api.jar
ENTRYPOINT ["java", "-jar", "song-api.jar"]