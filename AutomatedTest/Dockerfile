FROM eclipse-temurin:11-jre-alpine

WORKDIR /app
RUN chgrp -R 0 /app && \
    chmod -R g=u /app

COPY ./target/StaffnetIdentity-test-runner.jar .

ENTRYPOINT ["java","-Xss2048k","-jar","StaffnetIdentity-test-runner.jar"]
