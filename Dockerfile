FROM gradle:7-jdk17-alpine as gradle

ENV APP_DIR=/app
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
COPY . $APP_DIR

RUN gradle build

FROM openjdk:17-alpine

ENV APP_FILE=test-0.0.1-SNAPSHOT.jar
ENV APP_DIR=/app
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
COPY --from=gradle $APP_DIR/build/libs/$APP_FILE .

ENV PORT 8080
EXPOSE $PORT

CMD [ "sh", "-c", "java -jar -Dserver.port=${PORT} ${APP_FILE}" ]

