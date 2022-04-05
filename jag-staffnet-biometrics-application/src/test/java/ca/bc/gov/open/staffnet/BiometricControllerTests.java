package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.three.*;
import ca.bc.gov.open.staffnet.biometrics.three.DeactivateBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.two.DestroyBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.controllers.BiometricController;
import ca.bc.gov.open.staffnet.models.GetEnrolledWorkersOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BiometricControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testBiometricReconciliation() throws JsonProcessingException {
        var req = new BiometricReconciliation();
        BiometricReconciliationRequest biometricReconciliationRequest =
                new BiometricReconciliationRequest();
        biometricReconciliationRequest.setIndividualID("A");
        biometricReconciliationRequest.setRequestorUserId("A");
        biometricReconciliationRequest.setRequesterAccountTypeCode(
                BCeIDAccountTypeCode.BUSINESS.value());
        req.setBiometricReconciliationRequest(biometricReconciliationRequest);

        GetEnrolledWorkersOutput getEnrolledWorkersOutput = new GetEnrolledWorkersOutput();
        getEnrolledWorkersOutput.setWorkers(new ArrayList<ReconciliationItem>());
        getEnrolledWorkersOutput.setResponseCd("A");

        ResponseEntity<GetEnrolledWorkersOutput> responseEntity =
                new ResponseEntity<>(getEnrolledWorkersOutput, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetEnrolledWorkersOutput>>any()))
                .thenReturn(responseEntity);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.ReconciliationServiceResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.ReconciliationServiceResponse();
        ReconciliationServiceResponse reconciliationServiceResponse =
                new ReconciliationServiceResponse();
        reconciliationServiceResponse.setCode(ResponseCode.SUCCESS);
        reconciliationServiceResponse.setMessage("A");
        soapSvcResp.setReconciliationServiceResult(reconciliationServiceResponse);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(), Mockito.any(ReconciliationServiceRequest.class)))
                .thenReturn(soapSvcResp);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.biometricReconciliation(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testDeactivateBiometricCredentialByDID() throws Exception {
        var req = new DeactivateBiometricCredentialByDID();
        DeactivateBiometricCredentialByDIDRequest deactivateBiometricCredentialByDIDRequest =
                new DeactivateBiometricCredentialByDIDRequest();
        deactivateBiometricCredentialByDIDRequest.setDID("A");
        deactivateBiometricCredentialByDIDRequest.setRequestorAccountTypeCode(
                BCeIDAccountTypeCode.CORNET.value());
        deactivateBiometricCredentialByDIDRequest.setRequestorUserId("A");
        req.setDeactivateBiometricCredentialByDIDRequest(deactivateBiometricCredentialByDIDRequest);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.DeactivateBiometricCredentialByDIDResponse
                soapSvcResp =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .DeactivateBiometricCredentialByDIDResponse();
        ca.bc.gov.open.staffnet.biometrics.three.DeactivateBiometricCredentialByDIDResponse
                deactivateBiometricCredentialByDIDResponse =
                        new DeactivateBiometricCredentialByDIDResponse();
        deactivateBiometricCredentialByDIDResponse.setCode(ResponseCode.SUCCESS);
        deactivateBiometricCredentialByDIDResponse.setMessage("A");
        soapSvcResp.setDeactivateBiometricCredentialByDIDResult(
                deactivateBiometricCredentialByDIDResponse);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(), Mockito.any(DeactivateBiometricCredentialByDID.class)))
                .thenReturn(soapSvcResp);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.deactivateBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testDestroyBiometricCredentialByDID() throws JsonProcessingException {
        var req = new DestroyBiometricCredentialByDID();
        DestroyBiometricCredentialByDIDRequest destroyBiometricCredentialByDIDRequest =
                new DestroyBiometricCredentialByDIDRequest();
        destroyBiometricCredentialByDIDRequest.setDID("A");
        destroyBiometricCredentialByDIDRequest.setRequestorAccountTypeCode(
                BCeIDAccountTypeCode.VERIFIED_INDIVIDUAL.value());
        destroyBiometricCredentialByDIDRequest.setRequestorUserId("A");
        req.setDestroyBiometricCredentialByDIDRequest(destroyBiometricCredentialByDIDRequest);

        // Set up to mock soap service response
        DestroyBiometricCredentialByDIDResponse soapSvcResp =
                new DestroyBiometricCredentialByDIDResponse();
        ca.bc.gov.open.staffnet.biometrics.three.DestroyBiometricCredentialByDIDResponse
                destroyBiometricCredentialByDIDResponse =
                        new ca.bc.gov.open.staffnet.biometrics.three
                                .DestroyBiometricCredentialByDIDResponse();
        destroyBiometricCredentialByDIDResponse.setCode(ResponseCode.SUCCESS);
        destroyBiometricCredentialByDIDResponse.setMessage("A");
        soapSvcResp.setDestroyBiometricCredentialByDIDResult(
                destroyBiometricCredentialByDIDResponse);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two
                                        .DestroyBiometricCredentialByDID.class)))
                .thenReturn(soapSvcResp);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.destroyBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testReactivateBiometricCredentialByDID() throws JsonProcessingException {
        var req = new ReactivateBiometricCredentialByDID();
        ReactivateBiometricCredentialByDIDRequest reactivateBiometricCredentialByDIDRequest =
                new ReactivateBiometricCredentialByDIDRequest();
        reactivateBiometricCredentialByDIDRequest.setDID("A");
        reactivateBiometricCredentialByDIDRequest.setRequestorAccountTypeCode(
                BCeIDAccountTypeCode.VOID.value());
        reactivateBiometricCredentialByDIDRequest.setRequestorUserId("A");
        req.setReactivateBiometricCredentialByDIDRequest(reactivateBiometricCredentialByDIDRequest);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.ReactivateBiometricCredentialByDIDResponse
                soapSvcResp =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .ReactivateBiometricCredentialByDIDResponse();
        ca.bc.gov.open.staffnet.biometrics.three.ReactivateBiometricCredentialByDIDResponse
                reactivateBiometricCredentialByDIDResponse =
                        new ca.bc.gov.open.staffnet.biometrics.three
                                .ReactivateBiometricCredentialByDIDResponse();
        reactivateBiometricCredentialByDIDResponse.setCode(ResponseCode.SUCCESS);
        reactivateBiometricCredentialByDIDResponse.setMessage("A");
        soapSvcResp.setReactivateBiometricCredentialByDIDResult(
                reactivateBiometricCredentialByDIDResponse);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheck
                                        .class)))
                .thenReturn(soapSvcResp);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.reactivateBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }
}
