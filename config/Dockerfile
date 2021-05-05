FROM openjdk:8-jdk-alpine

COPY target/config-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "config-1.0-SNAPSHOT.jar"]

EXPOSE 8001