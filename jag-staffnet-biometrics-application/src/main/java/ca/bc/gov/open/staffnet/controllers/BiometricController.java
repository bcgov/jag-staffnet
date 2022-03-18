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
public class BiometricController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public BiometricController(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PayloadRoot(namespace = SoapConfig.SOAP_NAMESPACE, localPart = "biometricReconciliation")
    @ResponsePayload
    public BiometricReconciliationResponse biometricReconciliation(
            @RequestPayload BiometricReconciliation search) throws JsonProcessingException {
        var inner =
                search.getBiometricReconciliationRequest() != null
                        ? search.getBiometricReconciliationRequest()
                        : new BiometricReconciliationRequest();
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "bio/reconciliation");

        try {
            HttpEntity<BiometricReconciliationResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(new HttpHeaders()),
                            BiometricReconciliationResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "biometricReconciliation")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "biometricReconciliation",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = SoapConfig.SOAP_NAMESPACE,
            localPart = "deactivateBiometricCredentialByDID")
    @ResponsePayload
    public DeactivateBiometricCredentialByDIDResponse deactivateBiometricCredentialByDID(
            @RequestPayload DeactivateBiometricCredentialByDID search)
            throws JsonProcessingException {
        var inner =
                search.getDeactivateBiometricCredentialByDIDRequest() != null
                        ? search.getDeactivateBiometricCredentialByDIDRequest()
                        : new DeactivateBiometricCredentialByDIDRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "bio/deactivate");

        try {
            HttpEntity<DeactivateBiometricCredentialByDIDResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(new HttpHeaders()),
                            DeactivateBiometricCredentialByDIDResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "deactivateBiometricCredentialByDID")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "deactivateBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = SoapConfig.SOAP_NAMESPACE,
            localPart = "destroyBiometricCredentialByDID")
    @ResponsePayload
    public DestroyBiometricCredentialByDIDResponse destroyBiometricCredentialByDID(
            @RequestPayload DestroyBiometricCredentialByDID search) throws JsonProcessingException {
        var inner =
                search.getDestroyBiometricCredentialByDIDRequest() != null
                        ? search.getDestroyBiometricCredentialByDIDRequest()
                        : new DestroyBiometricCredentialByDIDRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "bio/destroy");

        try {
            HttpEntity<DestroyBiometricCredentialByDIDResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.DELETE,
                            new HttpEntity<>(new HttpHeaders()),
                            DestroyBiometricCredentialByDIDResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "destroyBiometricCredentialByDID")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "destroyBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }

    @PayloadRoot(
            namespace = SoapConfig.SOAP_NAMESPACE,
            localPart = "reactivateBiometricCredentialByDID")
    @ResponsePayload
    public ReactivateBiometricCredentialByDIDResponse reactivateBiometricCredentialByDID(
            @RequestPayload ReactivateBiometricCredentialByDID search)
            throws JsonProcessingException {
        var inner =
                search.getReactivateBiometricCredentialByDIDRequest() != null
                        ? search.getReactivateBiometricCredentialByDIDRequest()
                        : new ReactivateBiometricCredentialByDIDRequest();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + "bio/reactivate");

        try {
            HttpEntity<ReactivateBiometricCredentialByDIDResponse> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(new HttpHeaders()),
                            ReactivateBiometricCredentialByDIDResponse.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "reactivateBiometricCredentialByDID")));
            return resp.getBody();
        } catch (Exception ex) {
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from ORDS",
                                    "reactivateBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
            throw new ORDSException();
        }
    }
}
