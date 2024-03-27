# Staffnet

[![Lifecycle:Experimental](https://img.shields.io/badge/Lifecycle-Stable-97ca00)](https://github.com/bcgov/jag-staffnet)
[![Maintainability](https://api.codeclimate.com/v1/badges/a492f352f279a2d1621e/maintainability)](https://codeclimate.com/github/bcgov/jag-staffnet/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/a492f352f279a2d1621e/test_coverage)](https://codeclimate.com/github/bcgov/jag-staffnet/test_coverage)

### Recommended Tools
* Intellij
* Docker
* Maven
* Java 17
* Lombok

### Application Endpoints

Local Host: http://127.0.0.1:8080

Code Climate: https://codeclimate.com/github/bcgov/jag-staffnet

### Required Environmental Variables

BASIC_AUTH_PASS: The password for the basic authentication. This can be any value for local.

BASIC_AUTH_USER: The username for the basic authentication. This can be any value for local.

ORDS_HOST: The url for ords rest package.

ORDS_USERNAME: ORDS_HOST authentication

ORDS_PASSWORD: ORDS_HOST authentication

ONLINE_SERVICE_ID: Biometrics Online Service Id

WS_URL: webservice url to retrieve biometrics information

WS_AUTH_USER: web service username

WS_AUTH_PASS: web service password

WSDL Endpoint Local:
* localhost:8080/biometrics/StaffNet_Biometrics.ws.provider:SNBiometrics?WSDL

* localhost:8080/identity-provisioning/StaffNetIdentityProvisioning.ws.provider:StaffNetIdentityProvisioning?WSDL

### Optional Enviromental Variables
SPLUNK_HTTP_URL: The url for the splunk hec.

SPLUNK_TOKEN: The bearer token to authenticate the application.

SPLUNK_INDEX: The index that the application will push logs to. The index must be created in splunk
before they can be pushed to.

### Building the Application
1) Set intellij to use java 17 for the project modals and sdk
2) Run ``mvn compile``
3) Make sure ```target/generated-sources/xjc``` folder in included in module path for
```staffnet-common-models ```, ```staffnet-biometrics-models ```  and ```staffnet-identity-provisioning-models ```


### Running the application
Option A) Intellij
1) Create intellij run configuration from Staffnet-Identity-Provisioning and/or Staffnet-Biometrics Application
2) Set env variables. See the .env-template
3) Run the application

Option B) Jar
1) Run ```mvn package```
2) Run ```java -jar ./target/staffnet-biometrics-application.jar``` or ```java -jar ./target/staffnet-identity-provisioning-application.jar```

Option C) Docker (* being biometrics/identity-provisioning)
1) Run ```mvn package```
2) Run ```docker build -t staffnet-*-api .``` from root folder
3) Run ```docker run -p 8080:8080 staffnet-*-api```

Option D) Eclipse
1) Clone the project into a local folder.
2) Import the Maven project using the Maven Project Import Wizard.
3) Set Variables either as Windows/Linux Environmental variables or POM goal Environment Variables:

staffnet-biometrics-secrets.BASIC_AUTH_PASS

staffnet-biometrics-secrets.BASIC_AUTH_USER

staffnet-identity-provisioning-secrets.staffnet-biometrics-secrets.BASIC_AUTH_PASS

staffnet-identity-provisioning-secrets.staffnet-biometrics-secrets.BASIC_AUTH_USER

ORDS_HOST

ORDS_USERNAME

ORDS_PASSWORD

SPLUNK_HTTP_URL

SPLUNK_TOKEN

SPLUNK_INDEX

ONLINE_SERVICE_ID

WS_URL

WS_AUTH_USER

WS_AUTH_PASS

4) Create POM goals: clean install, spring-boot:run  (when running locally).

### Pre Commit
1) Do not commit \CRLF use unix line enders
2) Run the linter ```mvn spotless:apply```

### JaCoCo Coverage Report
1) Run ```mvn clean verify```
2) Open ```staffnet-code-coverage/target/site/jacoco-aggregate/index.html``` in a browser
