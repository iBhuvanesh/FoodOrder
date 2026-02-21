FROM maven:3.9.10-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -pl payment-service -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/payment-service/target/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/app/app.jar"]
