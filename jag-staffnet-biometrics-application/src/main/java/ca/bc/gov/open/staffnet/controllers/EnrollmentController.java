package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.three.ArrayOfIdentityName;
import ca.bc.gov.open.staffnet.biometrics.three.BCeIDAccountTypeCode;
import ca.bc.gov.open.staffnet.biometrics.three.IdentityName;
import ca.bc.gov.open.staffnet.biometrics.three.ResponseCode;
import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import ca.bc.gov.open.staffnet.models.*;
import ca.bc.gov.open.staffnet.models.serializers.InstantSoapConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "enrollment/start")
                        .queryParam("individualID", inner.getIndividualID());

        HttpEntity<WorkerInfoResponse> resp = null;
        try {
            resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            WorkerInfoResponse.class);
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
        startEnrollmentWithIdCheckRequest.setDateOfBirth(
                InstantSoapConverter.parse(resp.getBody().getDateOfBirth()));
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
            ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheckResponse soapSvcResp =
                    (ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheckResponse)
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
                    soapSvcResp.getStartEnrollmentWithIdCheckResult().getIssuance().getExpiry());
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
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "finishEnrollmentWithIdCheck")
    @ResponsePayload
    public FinishEnrollmentWithIdCheckResponse2 finishEnrollmentWithIdCheck(
            @RequestPayload FinishEnrollmentWithIdCheck search) throws JsonProcessingException {
        var inner =
                search.getFinishEnrollmentWithIdCheckRequest() != null
                        ? search.getFinishEnrollmentWithIdCheckRequest()
                        : new FinishEnrollmentWithIdCheckRequest();

        ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheck
                finishEnrollmentWithIdCheck =
                        new ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheck();
        ca.bc.gov.open.staffnet.biometrics.three.FinishEnrollmentWithIdCheckRequest
                finishEnrollmentWithIdCheckRequest =
                        new ca.bc.gov.open.staffnet.biometrics.three
                                .FinishEnrollmentWithIdCheckRequest();
        finishEnrollmentWithIdCheckRequest.setOnlineServiceId(onlineServiceId);
        finishEnrollmentWithIdCheckRequest.setRequesterUserId(inner.getRequestorUserId());
        finishEnrollmentWithIdCheckRequest.setRequesterAccountTypeCode(
                BCeIDAccountTypeCode.fromValue(inner.getRequestorAccountTypeCode()));
        finishEnrollmentWithIdCheckRequest.setIssuanceID(inner.getIssuanceID());
        finishEnrollmentWithIdCheck.setRequest(finishEnrollmentWithIdCheckRequest);

        FinishEnrollmentWithIdCheckResponse2 out = new FinishEnrollmentWithIdCheckResponse2();

        ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse soapSvcResp =
                null;
        // Invoke Soap Service
        try {
            soapSvcResp =
                    (ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse)
                            webServiceTemplate.marshalSendAndReceive(
                                    "http://www.bceid.ca/webservices/BCS/V4/FinishEnrollmentWithIdCheck",
                                    finishEnrollmentWithIdCheck);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "finishEnrollmentWithIdCheck")));
        } catch (Exception ex) {
            out.setCode(ResponseCode.FAILED.value());
            out.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - FinishEnrollmentWithIdCheck",
                                    "startEnrollmentWithIdCheck",
                                    ex.getMessage(),
                                    inner)));
            return out;
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "enrollment/finish");

        WorkerImageSetRequest req = new WorkerImageSetRequest();
        req.setIndiId(inner.getIndividualId());
        req.setUserId(inner.getRequestorUserId());
        req.setCallingModule("StaffNet");
        req.setPhoto(soapSvcResp.getFinishEnrollmentWithIdCheckResult().getPhoto());
        req.setPhotoTakenDate(
                InstantSoapConverter.parse(
                        soapSvcResp.getFinishEnrollmentWithIdCheckResult().getPhotoTakenDate()));
        HttpEntity<WorkerImageSetRequest> body = new HttpEntity<>(req, new HttpHeaders());
        try {
            HttpEntity<WorkerImageSetResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            body,
                            WorkerImageSetResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "finishEnrollmentWithIdCheck")));

            out.setImageSetSuccessYN(resp.getBody().getSuccessYN());
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
