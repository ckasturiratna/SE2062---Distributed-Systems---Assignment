FROM openjdk:11-jre-slim

WORKDIR /app

COPY metadata-service/target/metadata-service-*.jar app.jar

EXPOSE 8081 9001

ENTRYPOINT ["java", "-jar", "app.jar"]