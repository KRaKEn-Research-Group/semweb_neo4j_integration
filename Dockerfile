# Build stage
FROM maven:3.6.0-jdk-11-slim AS build
COPY src/semweb_middleware/src /home/app/src
COPY src/semweb_middleware/lib /home/app/lib
COPY src/semweb_middleware/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install

# Package stage
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/semweb_middleware-1.0.jar /usr/local/lib/semweb_middleware-1.0.jar
COPY --from=build /home/app/lib /home/app/lib
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/semweb_middleware-1.0.jar"]