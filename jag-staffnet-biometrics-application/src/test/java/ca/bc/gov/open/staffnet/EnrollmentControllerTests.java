package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.FinishEnrollmentWithIdCheck;
import ca.bc.gov.open.staffnet.biometrics.one.FinishEnrollmentWithIdCheckRequest;
import ca.bc.gov.open.staffnet.biometrics.one.StartEnrollmentWithIdCheck;
import ca.bc.gov.open.staffnet.biometrics.one.StartEnrollmentWithIdCheckRequest;
import ca.bc.gov.open.staffnet.biometrics.two.*;
import ca.bc.gov.open.staffnet.controllers.EnrollmentController;
import ca.bc.gov.open.staffnet.models.IdentityNameResponse;
import ca.bc.gov.open.staffnet.models.WorkerImageSetResponse;
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
public class EnrollmentControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Mock private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Test
    public void testStartEnrollmentWithIdCheck() throws JsonProcessingException {
        var req = new StartEnrollmentWithIdCheck();
        StartEnrollmentWithIdCheckRequest startEnrollmentWithIdCheckRequest =
                new StartEnrollmentWithIdCheckRequest();
        startEnrollmentWithIdCheckRequest.setDid("A");
        startEnrollmentWithIdCheckRequest.setRequesterAccountTypeCode(
                BCeIDAccountTypeCode.VERIFIED_INDIVIDUAL.value());
        startEnrollmentWithIdCheckRequest.setIndividualID("A");
        startEnrollmentWithIdCheckRequest.setRequestorUserId("A");
        req.setStartEnrollmentWithIdCheckRequest(startEnrollmentWithIdCheckRequest);

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
        startEnrollmentWithIdCheckResponse2.setCode(ResponseCode.SUCCESS);
        startEnrollmentWithIdCheckResponse2.setMessage("A");
        IssuanceToken issuanceToken = new IssuanceToken();
        issuanceToken.setIssuanceID("A");
        issuanceToken.setEnrollmentURL("A");
        issuanceToken.setExpiry("A");
        startEnrollmentWithIdCheckResponse2.setIssuance(issuanceToken);
        soapSvcResp.setStartEnrollmentWithIdCheckResult(startEnrollmentWithIdCheckResponse2);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two
                                        .ReactivateBiometricCredentialByDID.class)))
                .thenReturn(soapSvcResp);

        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);
        var out = enrollmentController.startEnrollmentWithIdCheck(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testFinishEnrollmentWithIdCheck() throws JsonProcessingException {
        var req = new FinishEnrollmentWithIdCheck();
        FinishEnrollmentWithIdCheckRequest finishEnrollmentWithIdCheckRequest =
                new FinishEnrollmentWithIdCheckRequest();
        finishEnrollmentWithIdCheckRequest.setIssuanceID("A");
        finishEnrollmentWithIdCheckRequest.setRequestorUserId("A");
        finishEnrollmentWithIdCheckRequest.setIdentityEventId("A");
        finishEnrollmentWithIdCheckRequest.setRequestorAccountTypeCode(
                BCeIDAccountTypeCode.BUSINESS.value());
        finishEnrollmentWithIdCheckRequest.setIndividualId("A");
        req.setFinishEnrollmentWithIdCheckRequest(finishEnrollmentWithIdCheckRequest);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse();
        ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse2
                finishEnrollmentWithIdCheckResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .FinishEnrollmentWithIdCheckResponse2();
        finishEnrollmentWithIdCheckResponse2.setMessage("A");
        finishEnrollmentWithIdCheckResponse2.setCode(ResponseCode.SUCCESS);
        finishEnrollmentWithIdCheckResponse2.setDid("A");
        finishEnrollmentWithIdCheckResponse2.setPhoto(new byte[0]);
        finishEnrollmentWithIdCheckResponse2.setDateOfBirth("A");
        finishEnrollmentWithIdCheckResponse2.setBiometricTemplateUrl("A");
        finishEnrollmentWithIdCheckResponse2.setGivenNames("A");
        finishEnrollmentWithIdCheckResponse2.setLastName("A");
        finishEnrollmentWithIdCheckResponse2.setPhotoTakenDate("A");
        soapSvcResp.setFinishEnrollmentWithIdCheckResult(finishEnrollmentWithIdCheckResponse2);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheck
                                        .class)))
                .thenReturn(soapSvcResp);

        WorkerImageSetResponse workerImageSetResponse = new WorkerImageSetResponse();
        workerImageSetResponse.setSuccessYN("A");
        ResponseEntity<WorkerImageSetResponse> responseEntity =
                new ResponseEntity<>(workerImageSetResponse, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<WorkerImageSetResponse>>any()))
                .thenReturn(responseEntity);

        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);
        var out = enrollmentController.finishEnrollmentWithIdCheck(req);
        Assertions.assertNotNull(out);
    }
}
