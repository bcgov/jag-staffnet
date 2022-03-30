package ca.bc.gov.open.staffnet.controllers;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.three.*;
import ca.bc.gov.open.staffnet.biometrics.two.ReconciliationService;
import ca.bc.gov.open.staffnet.configuration.SoapConfig;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import ca.bc.gov.open.staffnet.models.GetEnrolledWorkersOutput;
import ca.bc.gov.open.staffnet.models.OrdsErrorLog;
import ca.bc.gov.open.staffnet.models.RequestSuccessLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
public class BiometricController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    @Value("${staffnet.online-service-id}")
    private String onlineServiceId;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WebServiceTemplate webServiceTemplate;

    @Autowired
    public BiometricController(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            WebServiceTemplate webServiceTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.webServiceTemplate = webServiceTemplate;
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
            HttpEntity<GetEnrolledWorkersOutput> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetEnrolledWorkersOutput.class);

            BiometricReconciliationResponse out = new BiometricReconciliationResponse();
            BiometricReconciliationResponse2 biometricReconciliationResponse2 =
                    new BiometricReconciliationResponse2();
            out.setBiometricReconciliationResponse(biometricReconciliationResponse2);

            if (!resp.getBody().getResponseCd().equals("0")) {
                return out;
            }

            ReconciliationServiceRequest reconciliationServiceRequest =
                    new ReconciliationServiceRequest();
            reconciliationServiceRequest.setOnlineServiceId(onlineServiceId);
            reconciliationServiceRequest.setRequesterUserId(inner.getRequestorUserId());
            reconciliationServiceRequest.setRequesterAccountTypeCode(
                    BCeIDAccountTypeCode.fromValue(inner.getRequesterAccountTypeCode()));
            ArrayOfReconciliationItem arrayOfReconciliationItem =
                    new ArrayOfReconciliationItem();

            List<ReconciliationItem> workers = new ArrayList();
            for (var worker : resp.getBody().getWorkers()) {
                workers.add(worker);
            }
            arrayOfReconciliationItem.setReconciliationItem(workers);

            reconciliationServiceRequest.setReconciliationItems(arrayOfReconciliationItem);
            reconciliationServiceRequest.setReconciliationItems(
                    new ArrayOfReconciliationItem());
            ReconciliationService reconciliationService = new ReconciliationService();
            reconciliationService.setRequest(reconciliationServiceRequest);

            // Invoke Soap Service
            try {
                var req =
                        webServiceTemplate.marshalSendAndReceive(
                                "http://www.bceid.ca/webservices/BCS/V4/ReconciliationService",
                                reconciliationService);
            } catch (Exception ex) {
                biometricReconciliationResponse2.setResponseCd("Failed");
                biometricReconciliationResponse2.setResponseTxt(
                        "Unable to connect to Backend Database");
            }
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog("Request Success", "biometricReconciliation")));
            return out;
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

        DeactivateBiometricCredentialByDID deactivateBiometricCredentialByDID = new DeactivateBiometricCredentialByDID();
        DeactivateBiometricCredentialByDIDRequest deactivateBiometricCredentialByDIDRequest = new DeactivateBiometricCredentialByDIDRequest();
        deactivateBiometricCredentialByDIDRequest.setDID(inner.getDID());
        deactivateBiometricCredentialByDIDRequest.setRequestorAccountTypeCode(inner.setRequestorAccountTypeCode());
        deactivateBiometricCredentialByDIDRequest.setRequestorUserId(inner.getRequestorUserId());

        // Invoke Soap Service
        try {
            var req =
                    webServiceTemplate.marshalSendAndReceive(
                            "http://www.bceid.ca/webservices/BCS/V4/DeactivateBiometricCredentialByDID",
                            reconciliationService);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "deactivateBiometricCredentialByDID")));
            var out = new DeactivateBiometricCredentialByDIDResponse();
            out.setDeactivateBiometricCredentialByDIDResponse(resp.getBody());
            return out;
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
            HttpEntity<DestroyBiometricCredentialByDIDResponse2> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.DELETE,
                            new HttpEntity<>(new HttpHeaders()),
                            DestroyBiometricCredentialByDIDResponse2.class);
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "destroyBiometricCredentialByDID")));
            var out = new DestroyBiometricCredentialByDIDResponse();
            out.setDestroyBiometricCredentialByDIDResponse(resp.getBody());
            return out;
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
        // TODO: No ORDS call, SoapService needed
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
