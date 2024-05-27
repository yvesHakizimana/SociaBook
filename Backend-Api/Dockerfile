#Steps of building and packaging the java File.

# Build Stage (From The Base Image)
FROM maven:3.8.7-openjdk-18 AS build

#Definition of the workdirectory
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


#Runtime Stage
FROM amazoncorretto:17
#Definition of few things.
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/Backend-Api-*.jar /app/
EXPOSE 8090

ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV DB_URL=jdbc:postgresql://postgres:5432/book_social_network

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} Backend-Api-${JAR_VERSION}.jar





