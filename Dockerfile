# Build
FROM maven:3-openjdk-11 as target
WORKDIR /build
COPY ./ .
RUN mvn install package -DskipTests

# Run
FROM openjdk:11-jre
EXPOSE 8080
CMD exec java $JAVA_OPTS -jar /app.jar
COPY --from=target /build/game-manager-jk-web/target/game-manager-jk-web-0.0.1-SNAPSHOT.jar /app.jar