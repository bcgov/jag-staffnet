package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import ca.bc.gov.open.staffnet.models.OrdsErrorLog;
import ca.bc.gov.open.staffnet.models.RequestSuccessLog;
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
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Slf4j
public class SearchController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SearchController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "startSearchForIdentity")
    @ResponsePayload
    public StartSearchForIdentityResponse startSearchForIdentity(
            @RequestPayload StartSearchForIdentity search) throws JsonProcessingException {
        var inner =
                search.getStartSearchForIdentityRequest() != null
                        ? search.getStartSearchForIdentityRequest()
                        : new StartSearchForIdentityRequest();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "search/start")
                        .queryParam("requestorUserId", inner.getRequestorUserId())
                        .queryParam("requestorAccountTypeCode", inner.getRequestorAccountTypeCode())
                        .queryParam("activeOnly", inner.getActiveOnly());

        try {
            HttpEntity<StartSearchForIdentityResponse2> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            StartSearchForIdentityResponse2.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "startSearchForIdentity")));
            var out = new StartSearchForIdentityResponse();
            out.setStartSearchForIdentityResponse(resp.getBody());
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "startSearchForIdentity",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "finishSearchForIdentity")
    @ResponsePayload
    public FinishSearchForIdentityResponse finishSearchForIdentity(
            @RequestPayload FinishSearchForIdentity search) throws JsonProcessingException {
        var inner =
                search.getFinishSearchForIdentityRequest() != null
                        ? search.getFinishSearchForIdentityRequest()
                        : new FinishSearchForIdentityRequest();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "search/finish")
                        .queryParam("requestorUserId", inner.getRequestorUserId())
                        .queryParam("requestorAccountTypeCode", inner.getRequestorAccountTypeCode())
                        .queryParam("requesterUserGuid", inner.getRequesterUserGuid())
                        .queryParam("searchID", inner.getSearchID());

        try {
            HttpEntity<FinishSearchForIdentityResponse2> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            FinishSearchForIdentityResponse2.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "finishSearchForIdentity")));
            var out = new FinishSearchForIdentityResponse();
            out.setFinishSearchForIdentityResponse(resp.getBody());
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "finishSearchForIdentity",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
