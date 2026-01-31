FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

COPY ./pom.xml .
RUN mvn verify --fail-never

COPY ./src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar bff.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bff.jar"]
