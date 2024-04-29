# Build Stage.
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime Stage.
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

# Definition of few things.
WORKDIR /app
COPY --from=build /build/target/Backend-Api-${APP_VERSION}.jar /app/
EXPOSE 8080

ENV DB_URL=jdbc:postgresql://postgres:5432/book-social-network
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} Backend-Api-${JAR_VERSION}.jar
