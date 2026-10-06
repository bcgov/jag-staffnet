#FROM eclipse-temurin:17-jre-alpine
#
#RUN apk update && apk add --upgrade --no-cache expat  # fix CVE-2024-8176
#
#COPY ./target/staffnet-biometrics-application.jar staffnet-biometrics-application.jar
#
#ENTRYPOINT ["java","-jar","/staffnet-biometrics-application.jar"]


#############################################################################################
###              Stage where Docker is building spring boot app using maven               ###
#############################################################################################
FROM maven:3.9.9-eclipse-temurin-17 as build

ARG SKIP_TESTS=true
ARG MVN_PROFILE=default

WORKDIR /

COPY . .

RUN mvn clean install -Dmaven.test.skip=${SKIP_TESTS}

#############################################################################################

#############################################################################################
### Stage where Docker is running a java process to run a service built in previous stage ###
#############################################################################################
FROM eclipse-temurin:17-jre-alpine

RUN apk update \
    && apk add --upgrade --no-cache libexpat \
    && apk add --upgrade --no-cache libpng \
    && apk add --upgrade --no-cache openssl \
    && apk add --upgrade --no-cache gnutls

ARG SERVICE_NAME=jag-staffnet-biometrics-application

COPY --from=build ./${SERVICE_NAME}/target/*.jar /app/service.jar

CMD ["java", "-Xmx1g", "-jar", "/app/service.jar"]
#############################################################################################
