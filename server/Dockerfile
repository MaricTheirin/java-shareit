FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareit_server.jar
ENTRYPOINT ["java","-jar","/shareit_server.jar"]