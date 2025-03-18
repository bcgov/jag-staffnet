FROM eclipse-temurin:17-jre-alpine

RUN apk update && apk add --upgrade --no-cache expat  # fix CVE-2024-8176

COPY ./target/staffnet-biometrics-application.jar staffnet-biometrics-application.jar

ENTRYPOINT ["java","-jar","/staffnet-biometrics-application.jar"]
