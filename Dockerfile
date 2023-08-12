FROM openjdk:17-alpine

COPY build/libs/belajar-spring-restful-api-kotlin-0.0.1-SNAPSHOT.jar /app/application.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/application.jar"]