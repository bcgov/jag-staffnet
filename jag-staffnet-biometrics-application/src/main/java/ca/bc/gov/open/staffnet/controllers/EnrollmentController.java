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
public class EnrollmentController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public EnrollmentController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "startEnrollmentWithIdCheck")
    @ResponsePayload
    public StartEnrollmentWithIdCheckResponse startEnrollmentWithIdCheck(
            @RequestPayload StartEnrollmentWithIdCheck search) throws JsonProcessingException {
        var inner =
                search.getStartEnrollmentWithIdCheckRequest() != null
                        ? search.getStartEnrollmentWithIdCheckRequest()
                        : new StartEnrollmentWithIdCheckRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "start-enrollment");

        try {
            HttpEntity<StartEnrollmentWithIdCheckResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.POST,
                            new HttpEntity<>(new HttpHeaders()),
                            StartEnrollmentWithIdCheckResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "startEnrollmentWithIdCheck")));
            return resp.getBody();
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
            @RequestPayload FinishEnrollmentWithIdCheck search)
            throws JsonProcessingException {
        var inner =
                search.getFinishEnrollmentWithIdCheckRequest() != null
                        ? search.getFinishEnrollmentWithIdCheckRequest()
                        : new FinishEnrollmentWithIdCheckRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "finish-enrollment");

        try {
            HttpEntity<FinishEnrollmentWithIdCheckResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(new HttpHeaders()),
                            FinishEnrollmentWithIdCheckResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "finishEnrollmentWithIdCheck")));
            return resp.getBody();
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
