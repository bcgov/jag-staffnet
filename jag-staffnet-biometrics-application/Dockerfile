FROM eclipse-temurin:17-jre-alpine

RUN apk upgrade libexpat

COPY ./target/staffnet-biometrics-application.jar staffnet-biometrics-application.jar

ENTRYPOINT ["java","-jar","/staffnet-biometrics-application.jar"]
