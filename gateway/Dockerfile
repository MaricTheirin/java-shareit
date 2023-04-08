FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareit_gateway.jar
ENTRYPOINT ["java","-jar","/shareit_gateway.jar"]