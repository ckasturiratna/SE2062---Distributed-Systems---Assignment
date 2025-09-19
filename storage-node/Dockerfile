FROM openjdk:11-jre-slim

WORKDIR /app

COPY storage-node/target/storage-node-*.jar app.jar

RUN mkdir -p /app/data

EXPOSE 50051

ENTRYPOINT ["java", "-jar", "app.jar"]