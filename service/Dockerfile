FROM maven:3-eclipse-temurin-25 AS build
ADD . /project
WORKDIR /project
RUN mvn -e package

FROM eclipse-temurin:25-jre
COPY --from=build /project/target /app/target
ENTRYPOINT java -jar /app/target/codenames-0.0.1-SNAPSHOT.jar