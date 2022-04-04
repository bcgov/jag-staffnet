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

        BiometricReconciliationResponse out = new BiometricReconciliationResponse();
        BiometricReconciliationResponse2 two = new BiometricReconciliationResponse2();

        HttpEntity<GetEnrolledWorkersOutput> resp = null;
        try {
            resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.GET,
                            new HttpEntity<>(new HttpHeaders()),
                            GetEnrolledWorkersOutput.class);

            out.setBiometricReconciliationResponse(two);

            if (resp != null && resp.getBody() != null) {
                if (!resp.getBody().getResponseCd().equals("0")) {
                    return out;
                }
            }
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

        ReconciliationServiceRequest reconciliationServiceRequest =
                new ReconciliationServiceRequest();
        reconciliationServiceRequest.setOnlineServiceId(onlineServiceId);
        reconciliationServiceRequest.setRequesterUserId(inner.getRequestorUserId());
        reconciliationServiceRequest.setRequesterAccountTypeCode(
                BCeIDAccountTypeCode.fromValue(inner.getRequesterAccountTypeCode()));
        ArrayOfReconciliationItem arrayOfReconciliationItem = new ArrayOfReconciliationItem();

        List<ReconciliationItem> workers = new ArrayList();
        for (var worker : resp.getBody().getWorkers()) {
            workers.add(worker);
        }
        arrayOfReconciliationItem.setReconciliationItem(workers);

        reconciliationServiceRequest.setReconciliationItems(arrayOfReconciliationItem);
        ReconciliationService reconciliationService = new ReconciliationService();
        reconciliationService.setRequest(reconciliationServiceRequest);

        // Invoke Soap Service
        try {
            ca.bc.gov.open.staffnet.biometrics.two.ReconciliationServiceResponse soapSvcResp =
                    (ca.bc.gov.open.staffnet.biometrics.two.ReconciliationServiceResponse)
                            webServiceTemplate.marshalSendAndReceive(
                                    "http://www.bceid.ca/webservices/BCS/V4/ReconciliationService",
                                    reconciliationService);
            two.setResponseCd(soapSvcResp.getReconciliationServiceResult().getCode().value());
            two.setResponseTxt(soapSvcResp.getReconciliationServiceResult().getMessage());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "biometricReconciliation")));
        } catch (Exception ex) {
            two.setResponseCd(ResponseCode.FAILED.value());
            two.setResponseTxt("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - DeactivateBiometricCredentialByDID",
                                    "deactivateBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
        }
        return out;
    }

    @PayloadRoot(
            namespace = SoapConfig.SOAP_NAMESPACE,
            localPart = "deactivateBiometricCredentialByDID")
    @ResponsePayload
    public DeactivateBiometricCredentialByDIDResponse deactivateBiometricCredentialByDID(
            @RequestPayload DeactivateBiometricCredentialByDID search) throws Exception {
        var inner =
                search.getDeactivateBiometricCredentialByDIDRequest() != null
                        ? search.getDeactivateBiometricCredentialByDIDRequest()
                        : new DeactivateBiometricCredentialByDIDRequest();

        DeactivateBiometricCredentialByDID deactivateBiometricCredentialByDID =
                new DeactivateBiometricCredentialByDID();
        DeactivateBiometricCredentialByDIDRequest deactivateBiometricCredentialByDIDRequest =
                new DeactivateBiometricCredentialByDIDRequest();
        deactivateBiometricCredentialByDIDRequest.setDID(inner.getDID());
        deactivateBiometricCredentialByDIDRequest.setRequestorAccountTypeCode(
                inner.getRequestorAccountTypeCode());
        deactivateBiometricCredentialByDIDRequest.setRequestorUserId(inner.getRequestorUserId());
        deactivateBiometricCredentialByDID.setDeactivateBiometricCredentialByDIDRequest(
                deactivateBiometricCredentialByDIDRequest);

        DeactivateBiometricCredentialByDIDResponse out =
                new DeactivateBiometricCredentialByDIDResponse();
        DeactivateBiometricCredentialByDIDResponse2 two =
                new DeactivateBiometricCredentialByDIDResponse2();

        // Invoke Soap Service
        try {
            ca.bc.gov.open.staffnet.biometrics.two.DeactivateBiometricCredentialByDIDResponse
                    soapSvcResp =
                            (ca.bc.gov.open.staffnet.biometrics.two
                                            .DeactivateBiometricCredentialByDIDResponse)
                                    webServiceTemplate.marshalSendAndReceive(
                                            "http://www.bceid.ca/webservices/BCS/V4/DeactivateBiometricCredentialByDID",
                                            deactivateBiometricCredentialByDID);

            two.setMessage(soapSvcResp.getDeactivateBiometricCredentialByDIDResult().getMessage());
            two.setCode(
                    soapSvcResp.getDeactivateBiometricCredentialByDIDResult().getCode().value());
            two.setFailureCode(
                    soapSvcResp
                            .getDeactivateBiometricCredentialByDIDResult()
                            .getFailureCode()
                            .value());

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "deactivateBiometricCredentialByDID")));
        } catch (Exception ex) {
            two.setCode(ResponseCode.FAILED.value());
            two.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - DeactivateBiometricCredentialByDID",
                                    "deactivateBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
        }

        out.setDeactivateBiometricCredentialByDIDResponse(two);
        return out;
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

        ca.bc.gov.open.staffnet.biometrics.two.DestroyBiometricCredentialByDID
                destroyBiometricCredentialByDID =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .DestroyBiometricCredentialByDID();
        ca.bc.gov.open.staffnet.biometrics.three.DestroyBiometricCredentialByDIDRequest three =
                new ca.bc.gov.open.staffnet.biometrics.three
                        .DestroyBiometricCredentialByDIDRequest();
        three.setDID(inner.getDID());
        three.setOnlineServiceId(onlineServiceId);
        three.setRequesterAccountTypeCode(
                BCeIDAccountTypeCode.fromValue(inner.getRequestorAccountTypeCode()));
        three.setRequesterUserId(inner.getRequestorUserId());
        destroyBiometricCredentialByDID.setRequest(three);

        DestroyBiometricCredentialByDIDResponse out = new DestroyBiometricCredentialByDIDResponse();
        DestroyBiometricCredentialByDIDResponse2 two =
                new DestroyBiometricCredentialByDIDResponse2();
        out.setDestroyBiometricCredentialByDIDResponse(two);

        try {
            ca.bc.gov.open.staffnet.biometrics.two.DestroyBiometricCredentialByDIDResponse
                    soapSvcResp =
                            (ca.bc.gov.open.staffnet.biometrics.two
                                            .DestroyBiometricCredentialByDIDResponse)
                                    webServiceTemplate.marshalSendAndReceive(
                                            "http://www.bceid.ca/webservices/BCS/V4/DestroyBiometricCredentialByDID",
                                            destroyBiometricCredentialByDID);
            two.setCode(soapSvcResp.getDestroyBiometricCredentialByDIDResult().getCode().value());
            two.setMessage(soapSvcResp.getDestroyBiometricCredentialByDIDResult().getMessage());
            two.setFailureCode(
                    soapSvcResp
                            .getDestroyBiometricCredentialByDIDResult()
                            .getFailureCode()
                            .value());
            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "destroyBiometricCredentialByDID")));

        } catch (Exception ex) {
            two.setCode(ResponseCode.FAILED.value());
            two.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - DestroyBiometricCredentialByDID",
                                    "destroyBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
        }
        return out;
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

        ca.bc.gov.open.staffnet.biometrics.two.ReactivateBiometricCredentialByDID
                reactivateBiometricCredentialByDID =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .ReactivateBiometricCredentialByDID();
        ca.bc.gov.open.staffnet.biometrics.three.ReactivateBiometricCredentialByDIDRequest
                reactivateBiometricCredentialByDIDRequest =
                        new ca.bc.gov.open.staffnet.biometrics.three
                                .ReactivateBiometricCredentialByDIDRequest();
        reactivateBiometricCredentialByDIDRequest.setDID(inner.getDID());
        reactivateBiometricCredentialByDIDRequest.setRequesterAccountTypeCode(
                BCeIDAccountTypeCode.fromValue(inner.getRequestorAccountTypeCode()));
        reactivateBiometricCredentialByDIDRequest.setRequesterUserId(inner.getRequestorUserId());
        reactivateBiometricCredentialByDIDRequest.setOnlineServiceId(onlineServiceId);
        reactivateBiometricCredentialByDID.setRequest(reactivateBiometricCredentialByDIDRequest);

        ReactivateBiometricCredentialByDIDResponse out =
                new ReactivateBiometricCredentialByDIDResponse();
        ReactivateBiometricCredentialByDIDResponse2 two =
                new ReactivateBiometricCredentialByDIDResponse2();
        out.setReactivateBiometricCredentialByDIDResponse(two);

        try {
            ca.bc.gov.open.staffnet.biometrics.two.ReactivateBiometricCredentialByDIDResponse
                    soapSvcResp =
                            (ca.bc.gov.open.staffnet.biometrics.two
                                            .ReactivateBiometricCredentialByDIDResponse)
                                    webServiceTemplate.marshalSendAndReceive(
                                            "http://www.bceid.ca/webservices/BCS/V4/ReactivateBiometricCredentialByDID",
                                            reactivateBiometricCredentialByDID);
            two.setCode(
                    soapSvcResp.getReactivateBiometricCredentialByDIDResult().getCode().value());
            two.setMessage(soapSvcResp.getReactivateBiometricCredentialByDIDResult().getMessage());
            two.setFailureCode(
                    soapSvcResp
                            .getReactivateBiometricCredentialByDIDResult()
                            .getFailureCode()
                            .value());

            log.info(
                    objectMapper.writeValueAsString(
                            new RequestSuccessLog(
                                    "Request Success", "reactivateBiometricCredentialByDID")));
        } catch (Exception ex) {
            two.setCode(ResponseCode.FAILED.value());
            two.setMessage("Unable to connect to Backend Database");
            log.error(
                    objectMapper.writeValueAsString(
                            new OrdsErrorLog(
                                    "Error received from SOAP SERVICE - ReactivateBiometricCredentialByDID",
                                    "reactivateBiometricCredentialByDID",
                                    ex.getMessage(),
                                    inner)));
        }
        return out;
    }
}
