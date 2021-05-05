FROM openjdk:8-jdk-alpine

RUN apk update && apk add bash
COPY wait-for.sh /wait-for.sh
RUN chmod +x /wait-for.sh

COPY target/registry-1.0-SNAPSHOT.jar .
ENTRYPOINT ["/wait-for.sh", "config:8001", "--timeout=60", "--", "java", "-jar", "registry-1.0-SNAPSHOT.jar"]

EXPOSE 8761