FROM openjdk:16-jdk-alpine
VOLUME /tmp
ADD target/api-gateway-*.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar"]