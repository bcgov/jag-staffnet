package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.three.ArrayOfIdentityName;
import ca.bc.gov.open.staffnet.biometrics.three.BCeIDAccountTypeCode;
import ca.bc.gov.open.staffnet.biometrics.three.IdentityName;
import ca.bc.gov.open.staffnet.biometrics.three.ResponseCode;
import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import ca.bc.gov.open.staffnet.models.OrdsErrorLog;
import ca.bc.gov.open.staffnet.models.RequestSuccessLog;
import ca.bc.gov.open.staffnet.models.WorkerInfoResponse;
import ca.bc.gov.open.staffnet.models.serializers.InstantDeserializer;
import ca.bc.gov.open.staffnet.models.serializers.InstantSerializer;
import ca.bc.gov.open.staffnet.models.serializers.InstantSoapConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
@Slf4j
public class EnrollmentController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    @Value("${staffnet.online-service-id}")
    private String onlineServiceId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public EnrollmentController(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            WebServiceTemplate webServiceTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.webServiceTemplate = webServiceTemplate;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "startEnrollmentWithIdCheck")
    @ResponsePayload
    public StartEnrollmentWithIdCheckResponse startEnrollmentWithIdCheck(
            @RequestPayload StartEnrollmentWithIdCheck search) throws JsonProcessingException {
        var inner =
                search.getStartEnrollmentWithIdCheckRequest() != null
                        ? search.getStartEnrollmentWithIdCheckRequest()
                        : new StartEnrollmentWithIdCheckRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "enrollment/start")
                .queryParam("individualID", inner.getIndividualID());

        try {
            HttpEntity<WorkerInfoResponse>
                    resp =
                            restTemplate.exchange(
                                    builder.build().encode().toUri(),
                                    HttpMethod.GET,
                                    new HttpEntity<>(new HttpHeaders()),
                                    WorkerInfoResponse.class);
            ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheck
                    startEnrollmentWithIdCheck =
                            new ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheck();
            ca.bc.gov.open.staffnet.biometrics.three.StartEnrollmentWithIdCheckRequest
                    startEnrollmentWithIdCheckRequest =
                            new ca.bc.gov.open.staffnet.biometrics.three
                                    .StartEnrollmentWithIdCheckRequest();
            startEnrollmentWithIdCheckRequest.setOnlineServiceId(onlineServiceId);
            startEnrollmentWithIdCheckRequest.setRequesterUserId(inner.getRequestorUserId());
            startEnrollmentWithIdCheckRequest.setRequesterAccountTypeCode(
                    BCeIDAccountTypeCode.fromValue(inner.getRequesterAccountTypeCode()));
            startEnrollmentWithIdCheckRequest.setDid(inner.getDid());
            startEnrollmentWithIdCheckRequest.setDateOfBirth(InstantSoapConverter.parse(resp.getBody().getDateOfBirth()));
            startEnrollmentWithIdCheckRequest.setPhoto(resp.getBody().getPhotoBase64());
            List<IdentityName> identityNameList = resp.getBody().getIdentityNames();
            ArrayOfIdentityName arrayOfIdentityName = new ArrayOfIdentityName();
            arrayOfIdentityName.setIdentityName(identityNameList);
            startEnrollmentWithIdCheckRequest.setIdentityNames(arrayOfIdentityName);

            StartEnrollmentWithIdCheckResponse out = new StartEnrollmentWithIdCheckResponse();
            StartEnrollmentWithIdCheckResponse2 two = new StartEnrollmentWithIdCheckResponse2();
            out.setStartEnrollmentWithIdCheckResponse(two);
            // Invoke Soap Service
            try {
                ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheckResponse
                        soapSvcResp =
                                (ca.bc.gov.open.staffnet.biometrics.two
                                                .StartEnrollmentWithIdCheckResponse)
                                        webServiceTemplate.marshalSendAndReceive(
                                                "http://www.bceid.ca/webservices/BCS/V4/StartEnrollmentWithIdCheck",
                                                startEnrollmentWithIdCheck);
                two.setCode(soapSvcResp.getStartEnrollmentWithIdCheckResult().getCode().value());
                two.setMessage(soapSvcResp.getStartEnrollmentWithIdCheckResult().getMessage());
                two.setFailureCode(
                        soapSvcResp.getStartEnrollmentWithIdCheckResult().getFailureCode().value());
                two.setIssuanceId(
                        soapSvcResp
                                .getStartEnrollmentWithIdCheckResult()
                                .getIssuance()
                                .getIssuanceID());
                two.setExpiryDate(
                        soapSvcResp
                                .getStartEnrollmentWithIdCheckResult()
                                .getIssuance()
                                .getExpiry());
                two.setEnrollmentURL(
                        soapSvcResp
                                .getStartEnrollmentWithIdCheckResult()
                                .getIssuance()
                                .getEnrollmentURL());

                log.info(
                        objectMapper.writeValueAsString(
                                new RequestSuccessLog(
                                        "Request Success", "startEnrollmentWithIdCheck")));
            } catch (Exception ex) {
                two.setCode(ResponseCode.FAILED.value());
                two.setMessage("Unable to connect to Backend Database");
                log.error(
                        objectMapper.writeValueAsString(
                                new OrdsErrorLog(
                                        "Error received from SOAP SERVICE - StartEnrollmentWithIdCheck",
                                        "startEnrollmentWithIdCheck",
                                        ex.getMessage(),
                                        inner)));
            }
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "startEnrollmentWithIdCheck",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "finishEnrollmentWithIdCheck")
    @ResponsePayload
    public FinishEnrollmentWithIdCheckResponse finishEnrollmentWithIdCheck(
            @RequestPayload FinishEnrollmentWithIdCheck search) throws JsonProcessingException {
        var inner =
                search.getFinishEnrollmentWithIdCheckRequest() != null
                        ? search.getFinishEnrollmentWithIdCheckRequest()
                        : new FinishEnrollmentWithIdCheckRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "enrollment/finish");

        try {
            HttpEntity<FinishEnrollmentWithIdCheckResponse2> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(new HttpHeaders()),
                            FinishEnrollmentWithIdCheckResponse2.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "finishEnrollmentWithIdCheck")));
            var out = new FinishEnrollmentWithIdCheckResponse();
            out.setFinishEnrollmentWithIdCheckResponse(resp.getBody());
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "finishEnrollmentWithIdCheck",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
