package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.two.ActiveCodeRequest;
import ca.bc.gov.open.staffnet.biometrics.two.BCeIDAccountTypeCode;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.models.OrdsErrorLog;
import ca.bc.gov.open.staffnet.models.RequestSuccessLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class SearchController {
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
    public SearchController(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            WebServiceTemplate webServiceTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.webServiceTemplate = webServiceTemplate;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "startSearchForIdentity")
    @ResponsePayload
    public StartSearchForIdentityResponse startSearchForIdentity(
            @RequestPayload StartSearchForIdentity search) throws JsonProcessingException {
        var inner =
                search.getStartSearchForIdentityRequest() != null
                        ? search.getStartSearchForIdentityRequest()
                        : new StartSearchForIdentityRequest();

        ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentity startSearchForIdentity =
                new ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentity();
        ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityRequest
                startSearchForIdentityRequest =
                        new ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityRequest();
        startSearchForIdentityRequest.setOnlineServiceId(onlineServiceId);
        startSearchForIdentityRequest.setRequesterUserId(inner.getRequestorUserId());
        if (inner.getRequestorAccountTypeCode() != null) {
            startSearchForIdentityRequest.setRequesterAccountTypeCode(
                    BCeIDAccountTypeCode.fromValue(inner.getRequestorAccountTypeCode()));
        }
        if (inner.getActiveOnly() != null) {
            startSearchForIdentityRequest.setActiveOnly(
                    ActiveCodeRequest.fromValue(inner.getActiveOnly()));
        }
        startSearchForIdentity.setRequest(startSearchForIdentityRequest);

        StartSearchForIdentityResponse out = new StartSearchForIdentityResponse();
        StartSearchForIdentityResponse2 two = new StartSearchForIdentityResponse2();
        out.setStartSearchForIdentityResponse(two);
        // Invoke Soap Service
        try {
            ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityResponse soapSvcResp =
                    (ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityResponse)
                            webServiceTemplate.marshalSendAndReceive(wsUrl, startSearchForIdentity);
            two.setCode(soapSvcResp.getStartSearchForIdentityResult().getCode().value());
            two.setFailureCode(
                    soapSvcResp.getStartSearchForIdentityResult().getFailureCode().value());
            two.setMessage(soapSvcResp.getStartSearchForIdentityResult().getMessage());
            two.setSearchID(
                    soapSvcResp.getStartSearchForIdentityResult().getSearch().getSearchID());
            two.setSearchURL(
                    soapSvcResp.getStartSearchForIdentityResult().getSearch().getSearchURL());
            two.setExpiryDate(
                    soapSvcResp.getStartSearchForIdentityResult().getSearch().getExpiry());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "startSearchForIdentity")));
        } catch (Exception ex) {
            two.setCode(ResponseCode.FAILED.value());
            two.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - StartSearchForIdentity",
                                    "startSearchForIdentity",
                                    ex.getMessage(),
                                    inner)));
        }
        return out;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "finishSearchForIdentity")
    @ResponsePayload
    public FinishSearchForIdentityResponse finishSearchForIdentity(
            @RequestPayload FinishSearchForIdentity search) throws JsonProcessingException {
        var inner =
                search.getFinishSearchForIdentityRequest() != null
                        ? search.getFinishSearchForIdentityRequest()
                        : new FinishSearchForIdentityRequest();

        ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentity finishSearchForIdentity =
                new ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentity();
        ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityRequest
                finishSearchForIdentityRequest =
                        new ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityRequest();
        finishSearchForIdentityRequest.setOnlineServiceId(onlineServiceId);
        finishSearchForIdentityRequest.setRequesterUserId(inner.getRequestorUserId());
        if (inner.getRequestorAccountTypeCode() != null) {
            finishSearchForIdentityRequest.setRequesterAccountTypeCode(
                    BCeIDAccountTypeCode.fromValue(inner.getRequestorAccountTypeCode()));
        }
        finishSearchForIdentityRequest.setSearchID(inner.getSearchID());
        finishSearchForIdentity.setRequest(finishSearchForIdentityRequest);

        FinishSearchForIdentityResponse out = new FinishSearchForIdentityResponse();
        FinishSearchForIdentityResponse2 two = new FinishSearchForIdentityResponse2();
        out.setFinishSearchForIdentityResponse(two);
        // Invoke Soap Service
        try {
            ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityResponse soapSvcResp =
                    (ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityResponse)
                            webServiceTemplate.marshalSendAndReceive(
                                    wsUrl, finishSearchForIdentity);
            two.setCode(soapSvcResp.getFinishSearchForIdentityResult().getDID());
            two.setFailureCode(
                    soapSvcResp.getFinishSearchForIdentityResult().getFailureCode().value());
            two.setMessage(soapSvcResp.getFinishSearchForIdentityResult().getMessage());
            two.setStatus(soapSvcResp.getFinishSearchForIdentityResult().getStatus().value());
            two.setDID(soapSvcResp.getFinishSearchForIdentityResult().getDID());
            two.setActive(soapSvcResp.getFinishSearchForIdentityResult().getActive().value());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "finishSearchForIdentity")));
        } catch (Exception ex) {
            two.setCode(ResponseCode.FAILED.value());
            two.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - FinishSearchForIdentity",
                                    "finishSearchForIdentity",
                                    ex.getMessage(),
                                    inner)));
        }
        return out;
    }
}
