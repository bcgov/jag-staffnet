package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.two.ArrayOfIdentityName;
import ca.bc.gov.open.staffnet.biometrics.two.BCeIDAccountTypeCode;
import ca.bc.gov.open.staffnet.biometrics.two.IdentityName;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import ca.bc.gov.open.staffnet.models.OrdsErrorLog;
import ca.bc.gov.open.staffnet.models.RequestSuccessLog;
import ca.bc.gov.open.staffnet.models.WorkerInfoResponse;
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
public class RefreshController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    @Value("${staffnet.online-service-id}")
    private String onlineServiceId;

    @Value("${staffnet.web-service-url}")
    private String wsUrl = "";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public RefreshController(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            WebServiceTemplate webServiceTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.webServiceTemplate = webServiceTemplate;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "refreshIdentityWithIdCheck")
    @ResponsePayload
    public RefreshIdentityWithIdCheckResponse refreshIdentityWithIdCheck(
            @RequestPayload RefreshIdentityWithIdCheck search) throws JsonProcessingException {
        var inner =
                search.getRefreshIdentityWithIdCheckRequest() != null
                        ? search.getRefreshIdentityWithIdCheckRequest()
                        : new RefreshIdentityWithIdCheckRequest();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "refresh")
                        .queryParam("individualID", inner.getIndividualID());

        ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheck
                refreshIdentityWithIdCheck =
                        new ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheck();
        ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheckRequest
                refreshIdentityWithIdCheckRequest =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .RefreshIdentityWithIdCheckRequest();

        HttpEntity<WorkerInfoResponse> resp = null;
        try {
            resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            WorkerInfoResponse.class);

            refreshIdentityWithIdCheckRequest.setOnlineServiceId(onlineServiceId);
            refreshIdentityWithIdCheckRequest.setRequesterUserId(inner.getRequestorUserId());
            if (inner.getRequesterAccountTypeCode() != null) {
                refreshIdentityWithIdCheckRequest.setRequesterAccountTypeCode(
                        BCeIDAccountTypeCode.fromValue(inner.getRequesterAccountTypeCode()));
            }
            refreshIdentityWithIdCheckRequest.setDid(resp.getBody().getDid());
            refreshIdentityWithIdCheckRequest.setPhoto(resp.getBody().getPhotoBase64());
            refreshIdentityWithIdCheckRequest.setDateOfBirth(resp.getBody().getDateOfBirth());
            List<IdentityName> identityNameList = resp.getBody().getIdentityNames();
            ArrayOfIdentityName arrayOfIdentityName = new ArrayOfIdentityName();
            arrayOfIdentityName.getIdentityName().addAll(identityNameList);
            refreshIdentityWithIdCheckRequest.setIdentityNames(arrayOfIdentityName);
            refreshIdentityWithIdCheck.setRequest(refreshIdentityWithIdCheckRequest);
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "refreshIdentityWithIdCheck",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }

        RefreshIdentityWithIdCheckResponse out = new RefreshIdentityWithIdCheckResponse();
        RefreshIdentityWithIdCheckResponse2 two = new RefreshIdentityWithIdCheckResponse2();
        out.setRefreshIdentityWithIdCheckResponse(two);
        // Invoke Soap Service
        try {
            ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheckResponse soapSvcResp =
                    (ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheckResponse)
                            webServiceTemplate.marshalSendAndReceive(
                                    wsUrl, refreshIdentityWithIdCheck);
            two.setCode(soapSvcResp.getRefreshIdentityWithIdCheckResult().getCode().value());
            two.setMessage(soapSvcResp.getRefreshIdentityWithIdCheckResult().getMessage());
            two.setEnrollmentURL(
                    soapSvcResp
                            .getRefreshIdentityWithIdCheckResult()
                            .getIssuance()
                            .getEnrollmentURL());
            two.setExpiryDate(
                    soapSvcResp.getRefreshIdentityWithIdCheckResult().getIssuance().getExpiry());
            two.setIssuanceId(
                    soapSvcResp
                            .getRefreshIdentityWithIdCheckResult()
                            .getIssuance()
                            .getIssuanceID());
            two.setFailureCode(
                    soapSvcResp.getRefreshIdentityWithIdCheckResult().getFailureCode().value());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "refreshIdentityWithIdCheck")));
        } catch (Exception ex) {
            two.setCode(ResponseCode.FAILED.value());
            two.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - RefreshIdentityWithIdCheck",
                                    "refreshIdentityWithIdCheck",
                                    ex.getMessage(),
                                    refreshIdentityWithIdCheck.getRequest())));
        }
        return out;
    }
}
