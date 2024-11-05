FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Connect4Api-0.0.1-SNAPSHOT.jar Connect4Api.jar
EXPOSE 8800
ENTRYPOINT ["java", "-jar", "Connect4Api.jar"]