package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDID;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDID;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDID;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDIDRequest;
import ca.bc.gov.open.staffnet.biometrics.two.*;
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
        ReconciliationServiceResponse2 reconciliationServiceResponse2 =
                new ReconciliationServiceResponse2();
        reconciliationServiceResponse2.setCode(ResponseCode.SUCCESS);
        reconciliationServiceResponse2.setMessage("A");
        soapSvcResp.setReconciliationServiceResult(reconciliationServiceResponse2);
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
        ca.bc.gov.open.staffnet.biometrics.two.DeactivateBiometricCredentialByDIDResponse2
                deactivateBiometricCredentialByDIDResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .DeactivateBiometricCredentialByDIDResponse2();
        deactivateBiometricCredentialByDIDResponse2.setCode(ResponseCode.SUCCESS);
        deactivateBiometricCredentialByDIDResponse2.setMessage("A");
        soapSvcResp.setDeactivateBiometricCredentialByDIDResult(
                deactivateBiometricCredentialByDIDResponse2);
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
        ca.bc.gov.open.staffnet.biometrics.two.DestroyBiometricCredentialByDIDResponse2
                destroyBiometricCredentialByDIDResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .DestroyBiometricCredentialByDIDResponse2();
        destroyBiometricCredentialByDIDResponse2.setCode(ResponseCode.SUCCESS);
        destroyBiometricCredentialByDIDResponse2.setMessage("A");
        soapSvcResp.setDestroyBiometricCredentialByDIDResult(
                destroyBiometricCredentialByDIDResponse2);
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
        ca.bc.gov.open.staffnet.biometrics.two.ReactivateBiometricCredentialByDIDResponse2
                reactivateBiometricCredentialByDIDResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .ReactivateBiometricCredentialByDIDResponse2();
        reactivateBiometricCredentialByDIDResponse2.setCode(ResponseCode.SUCCESS);
        reactivateBiometricCredentialByDIDResponse2.setMessage("A");
        soapSvcResp.setReactivateBiometricCredentialByDIDResult(
                reactivateBiometricCredentialByDIDResponse2);
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
