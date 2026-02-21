FROM maven:3.9.10-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -pl restaurant-service -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/restaurant-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
