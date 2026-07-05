FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./pom.xml
COPY car-service/pom.xml ./car-service/pom.xml
COPY service-service/pom.xml ./service-service/pom.xml
COPY audit-service/pom.xml ./audit-service/pom.xml
COPY car-service/src ./car-service/src
COPY service-service/src ./service-service/src
COPY audit-service/src ./audit-service/src
RUN mvn -pl service-service -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/service-service/target/service-service-0.1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
