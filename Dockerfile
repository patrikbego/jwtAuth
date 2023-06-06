FROM alpine:3.18.0

RUN apk update && apk upgrade \
    && apk add openjdk17 net-tools

WORKDIR /app

COPY target/jwtAuth-0.0.1-SNAPSHOT.jar /app/jwtAuth.jar

EXPOSE 8080

CMD java -jar jwtAuth.jar
