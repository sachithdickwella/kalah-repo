FROM openjdk:11.0.5-jre-stretch

LABEL author="Sachith Dickwella"
LABEL name="Play Kalah"
LABEL version="1.0.0-SNAPSHOT"

ENV REDIS_HOST redis

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} kalah-game.jar
ENTRYPOINT java -jar kalah-game.jar

EXPOSE 8080