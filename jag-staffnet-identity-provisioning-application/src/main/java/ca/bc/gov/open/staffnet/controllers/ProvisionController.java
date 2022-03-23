package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import ca.bc.gov.open.staffnet.identity_provisioning.one.*;
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
public class ProvisionController {

    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProvisionController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "getProvisionedWorkers")
    @ResponsePayload
    public GetProvisionedWorkersResponse getProvisionedWorkers(
            @RequestPayload GetProvisionedWorkers search) throws JsonProcessingException {
        var inner =
                search.getGetProvisionedWorkerRequest() != null
                        ? search.getGetProvisionedWorkerRequest()
                        : new GetProvisionedWorkerRequest();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "workers")
                        .queryParam("locationCd", inner.getLocationCd());

        try {
            HttpEntity<GetProvisionedWorkerResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetProvisionedWorkerResponse.class);

            var out = new GetProvisionedWorkersResponse();
            out.setGetProvisionedWorkerResponse(resp.getBody());

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "getProvisionedWorkers")));
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getProvisionedWorkers",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = SoapConfig.SOAP_NAMESPACE,
            localPart = "getWorkerProvisioningQueueItem")
    @ResponsePayload
    public GetWorkerProvisioningQueueItemResponse getWorkerProvisioningQueueItem(
            @RequestPayload GetWorkerProvisioningQueueItem search) throws JsonProcessingException {
        var inner =
                search.getGetWorkerProvisioningQueueItemRequest() != null
                        ? search.getGetWorkerProvisioningQueueItemRequest()
                        : new GetWorkerProvisioningQueueItemRequest();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "worker/queueitem")
                        .queryParam("locationCd", inner.getLocationCd());

        try {
            HttpEntity<GetWorkerProvisioningQueueItemResponse2> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetWorkerProvisioningQueueItemResponse2.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "getWorkerProvisioningQueueItem")));
            var out = new GetWorkerProvisioningQueueItemResponse();
            out.setGetWorkerProvisioningQueueItemResponse(resp.getBody());
            return out;
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "getWorkerProvisioningQueueItem",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "setWorkerProvisioningStatus")
    @ResponsePayload
    public SetWorkerProvisioningStatusResponse setWorkerProvisioningStatus(
            @RequestPayload SetWorkerProvisioningStatus search) throws JsonProcessingException {
        var inner =
                search.getSetWorkerProvisioningStatusRequest() != null
                        ? search.getSetWorkerProvisioningStatusRequest()
                        : new SetWorkerProvisioningStatusRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "worker/status");

        try {
            HttpEntity<SetWorkerProvisioningStatusResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(new HttpHeaders()),
                            SetWorkerProvisioningStatusResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "setWorkerProvisioningStatus")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "setWorkerProvisioningStatus",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
