package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDID;
import ca.bc.gov.open.staffnet.biometrics.one.DeactivateBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDID;
import ca.bc.gov.open.staffnet.biometrics.one.DestroyBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.one.FinishEnrollmentWithIdCheck;
import ca.bc.gov.open.staffnet.biometrics.one.FinishEnrollmentWithIdCheckResponse;
import ca.bc.gov.open.staffnet.biometrics.one.FinishSearchForIdentity;
import ca.bc.gov.open.staffnet.biometrics.one.FinishSearchForIdentityResponse;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDID;
import ca.bc.gov.open.staffnet.biometrics.one.ReactivateBiometricCredentialByDIDResponse;
import ca.bc.gov.open.staffnet.biometrics.one.RefreshIdentityWithIdCheck;
import ca.bc.gov.open.staffnet.biometrics.one.RefreshIdentityWithIdCheckRequest;
import ca.bc.gov.open.staffnet.biometrics.one.RefreshIdentityWithIdCheckResponse;
import ca.bc.gov.open.staffnet.biometrics.one.StartEnrollmentWithIdCheck;
import ca.bc.gov.open.staffnet.biometrics.one.StartEnrollmentWithIdCheckRequest;
import ca.bc.gov.open.staffnet.biometrics.one.StartEnrollmentWithIdCheckResponse;
import ca.bc.gov.open.staffnet.biometrics.one.StartSearchForIdentity;
import ca.bc.gov.open.staffnet.biometrics.one.StartSearchForIdentityResponse;
import ca.bc.gov.open.staffnet.biometrics.two.*;
import ca.bc.gov.open.staffnet.controllers.BiometricController;
import ca.bc.gov.open.staffnet.controllers.EnrollmentController;
import ca.bc.gov.open.staffnet.controllers.RefreshController;
import ca.bc.gov.open.staffnet.controllers.SearchController;
import ca.bc.gov.open.staffnet.models.GetEnrolledWorkersOutput;
import ca.bc.gov.open.staffnet.models.IdentityNameResponse;
import ca.bc.gov.open.staffnet.models.WorkerInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SoapSvcErrorTests {
    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Mock private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testBiometricReconciliationSoapServiceFail() throws JsonProcessingException {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);

        GetEnrolledWorkersOutput getEnrolledWorkersOutput = new GetEnrolledWorkersOutput();
        getEnrolledWorkersOutput.setWorkers(new ArrayList<ReconciliationItem>());
        getEnrolledWorkersOutput.setResponseCd(ResponseCode.FAILED.value());
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
        reconciliationServiceResponse2.setCode(ResponseCode.FAILED);
        soapSvcResp.setReconciliationServiceResult(reconciliationServiceResponse2);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(), Mockito.any(ReconciliationServiceRequest.class)))
                .thenReturn(soapSvcResp);

        BiometricReconciliation req = new BiometricReconciliation();
        BiometricReconciliationRequest biometricReconciliationRequest =
                new BiometricReconciliationRequest();
        biometricReconciliationRequest.setRequesterAccountTypeCode("Business");
        req.setBiometricReconciliationRequest(biometricReconciliationRequest);
        BiometricReconciliationResponse out = biometricController.biometricReconciliation(req);
        Assertions.assertEquals(
                ResponseCode.FAILED.value(),
                out.getBiometricReconciliationResponse().getResponseCd());
    }

    @Test
    public void testDeactivateBiometricCredentialByDIDSoapServiceFail() throws Exception {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        DeactivateBiometricCredentialByDIDResponse out =
                biometricController.deactivateBiometricCredentialByDID(
                        new DeactivateBiometricCredentialByDID());
        Assertions.assertEquals(
                ResponseCode.FAILED.value(),
                out.getDeactivateBiometricCredentialByDIDResponse().getCode());
    }

    @Test
    public void testDestroyBiometricCredentialByDIDSoapServiceFail()
            throws JsonProcessingException {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        DestroyBiometricCredentialByDIDResponse out =
                biometricController.destroyBiometricCredentialByDID(
                        new DestroyBiometricCredentialByDID());
        Assertions.assertEquals(
                ResponseCode.FAILED.value(),
                out.getDestroyBiometricCredentialByDIDResponse().getCode());
    }

    @Test
    public void testReactivateBiometricCredentialByDIDSoapServiceFail()
            throws JsonProcessingException {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        ReactivateBiometricCredentialByDIDResponse out =
                biometricController.reactivateBiometricCredentialByDID(
                        new ReactivateBiometricCredentialByDID());
        Assertions.assertEquals(
                ResponseCode.FAILED.value(),
                out.getReactivateBiometricCredentialByDIDResponse().getCode());
    }

    @Test
    public void testStartEnrollmentWithIdCheckSoapServiceFail() throws JsonProcessingException {
        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);

        WorkerInfoResponse workerInfoResponse = new WorkerInfoResponse();
        workerInfoResponse.setDid("A");
        workerInfoResponse.setResponseCode(ResponseCode.SUCCESS);
        workerInfoResponse.setDateOfBirth("A");
        workerInfoResponse.setPhotoBase64(new byte[0]);
        List<IdentityNameResponse> identityNameList = new ArrayList<>();
        IdentityNameResponse identityName = new IdentityNameResponse();
        identityName.setGivenName("A");
        identityName.setLastName("A");
        identityName.setMiddleName("A");
        identityName.setType("LEGAL");
        identityNameList.add(identityName);
        workerInfoResponse.setIdentityNames(identityNameList);
        ResponseEntity<WorkerInfoResponse> responseEntity =
                new ResponseEntity<>(workerInfoResponse, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<WorkerInfoResponse>>any()))
                .thenReturn(responseEntity);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheckResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheckResponse();
        ca.bc.gov.open.staffnet.biometrics.two.StartEnrollmentWithIdCheckResponse2
                startEnrollmentWithIdCheckResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .StartEnrollmentWithIdCheckResponse2();
        startEnrollmentWithIdCheckResponse2.setCode(ResponseCode.FAILED);
        soapSvcResp.setStartEnrollmentWithIdCheckResult(startEnrollmentWithIdCheckResponse2);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two
                                        .ReactivateBiometricCredentialByDID.class)))
                .thenReturn(soapSvcResp);

        StartEnrollmentWithIdCheck req = new StartEnrollmentWithIdCheck();
        StartEnrollmentWithIdCheckRequest startEnrollmentWithIdCheckRequest =
                new StartEnrollmentWithIdCheckRequest();
        startEnrollmentWithIdCheckRequest.setRequesterAccountTypeCode("Business");
        req.setStartEnrollmentWithIdCheckRequest(startEnrollmentWithIdCheckRequest);
        StartEnrollmentWithIdCheckResponse out =
                enrollmentController.startEnrollmentWithIdCheck(req);
        Assertions.assertEquals(
                ResponseCode.FAILED.value(), out.getStartEnrollmentWithIdCheckResponse().getCode());
    }

    @Test
    public void testFinishEnrollmentWithIdCheckSoapServiceFail() throws JsonProcessingException {
        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);
        FinishEnrollmentWithIdCheckResponse out =
                enrollmentController.finishEnrollmentWithIdCheck(new FinishEnrollmentWithIdCheck());
        Assertions.assertEquals(
                ResponseCode.FAILED.value(),
                out.getFinishEnrollmentWithIdCheckResponse().getCode());
    }

    @Test
    public void testStartSearchForIdentitySoapServiceFail() throws JsonProcessingException {
        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);
        StartSearchForIdentityResponse out =
                searchController.startSearchForIdentity(new StartSearchForIdentity());
        Assertions.assertEquals(
                ResponseCode.FAILED.value(), out.getStartSearchForIdentityResponse().getCode());
    }

    @Test
    public void testFinishSearchForIdentitySoapServiceFail() throws JsonProcessingException {
        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);
        FinishSearchForIdentityResponse out =
                searchController.finishSearchForIdentity(new FinishSearchForIdentity());
        Assertions.assertEquals(
                ResponseCode.FAILED.value(), out.getFinishSearchForIdentityResponse().getCode());
    }

    @Test
    public void testRefreshIdentityWithIdCheckSoapServiceFail() throws JsonProcessingException {
        RefreshController refreshController =
                new RefreshController(restTemplate, objectMapper, webServiceTemplate);

        WorkerInfoResponse workerInfoResponse = new WorkerInfoResponse();
        List<IdentityNameResponse> identityNameList = new ArrayList<>();
        IdentityNameResponse identityName = new IdentityNameResponse();
        identityName.setGivenName("A");
        identityName.setLastName("A");
        identityName.setMiddleName("A");
        identityName.setType("LEGAL");
        identityNameList.add(identityName);
        workerInfoResponse.setIdentityNames(identityNameList);
        workerInfoResponse.setResponseCode(ResponseCode.SUCCESS);
        workerInfoResponse.setPhotoBase64(new byte[0]);
        workerInfoResponse.setDid("A");
        workerInfoResponse.setDateOfBirth("A");
        workerInfoResponse.setResponseMessage("A");

        ResponseEntity<WorkerInfoResponse> responseEntity =
                new ResponseEntity<>(workerInfoResponse, HttpStatus.OK);
        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<WorkerInfoResponse>>any()))
                .thenReturn(responseEntity);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheckResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheckResponse();
        ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheckResponse2
                refreshIdentityWithIdCheckResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .RefreshIdentityWithIdCheckResponse2();
        refreshIdentityWithIdCheckResponse2.setCode(ResponseCode.FAILED);
        soapSvcResp.setRefreshIdentityWithIdCheckResult(refreshIdentityWithIdCheckResponse2);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheck
                                        .class)))
                .thenReturn(soapSvcResp);

        RefreshIdentityWithIdCheck req = new RefreshIdentityWithIdCheck();
        RefreshIdentityWithIdCheckRequest refreshIdentityWithIdCheckRequest =
                new RefreshIdentityWithIdCheckRequest();
        refreshIdentityWithIdCheckRequest.setRequesterAccountTypeCode("Business");
        req.setRefreshIdentityWithIdCheckRequest(refreshIdentityWithIdCheckRequest);
        RefreshIdentityWithIdCheckResponse out = refreshController.refreshIdentityWithIdCheck(req);
        Assertions.assertEquals(
                ResponseCode.FAILED.value(), out.getRefreshIdentityWithIdCheckResponse().getCode());
    }
}
