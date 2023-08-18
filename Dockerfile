FROM openjdk:17-alpine

ENV APP_PORT=8080

COPY build/libs/belajar-spring-restful-api-kotlin-0.0.1-SNAPSHOT.jar /app/application.jar

EXPOSE $APP_PORT

CMD java -jar /app/application.jar --server.port=$APP_PORT