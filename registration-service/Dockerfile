FROM openjdk:8-jdk-alpine

RUN apk update && apk add bash
COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh

COPY target/registration-service-1.0-SNAPSHOT.jar .
ENTRYPOINT ["/wait-for.sh", "gateway:8081", "--timeout=60", "--", "java", "-jar", "registration-service-1.0-SNAPSHOT.jar"]

EXPOSE 8082