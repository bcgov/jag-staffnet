FROM eclipse-temurin:17-jre-alpine

RUN apk upgrade libexpat

COPY ./target/staffnet-identity-provisioning-application.jar staffnet-identity-provisioning-application.jar

ENTRYPOINT ["java","-jar","/staffnet-identity-provisioning-application.jar"]
