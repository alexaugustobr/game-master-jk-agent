FROM debian:11
RUN apt update && apt install \
    wget \ 
    docker \
    docker.io \ 
    docker-compose \ 
    openjdk-11-jre --no-install-recommends -y && apt clean && rm -rf /var/lib/apt/lists/*
EXPOSE 8080
WORKDIR /app
CMD exec java $JAVA_OPTS -jar /app/app.jar --spring.config.location=file:///app/application.properties
COPY game-manager-jk-api/target/game-manager-jk-api-*.jar /app/app.jar