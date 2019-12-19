FROM openjdk:11 AS BUILD_IMAGE
ARG VERSION=0.0.1-SNAPSHOT
ARG COMMAND=build
ARG PROFILE=dev
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY . .
RUN ./mvnw clean package

FROM openjdk:11
ARG VERSION
ENV ARTIFACT_NAME=orders-example-${VERSION}.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY --from=BUILD_IMAGE $APP_HOME/target/$ARTIFACT_NAME orders.jar
EXPOSE 8080
ENTRYPOINT ["java","-Duser.timezone=UTC", "-jar","orders.jar"]
