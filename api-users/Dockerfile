FROM openjdk:16-jdk-alpine
EXPOSE 8082
VOLUME /tmp
ADD build/libs/api-users-0.0.1-SNAPSHOT-plain.jar api-users-0.0.1-SNAPSHOT-plain.jar
ENTRYPOINT ["java","-jar","/api-users-0.0.1-SNAPSHOT-plain.jar"]