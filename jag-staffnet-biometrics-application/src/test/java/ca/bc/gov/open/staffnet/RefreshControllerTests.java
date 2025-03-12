package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.two.IdentityName;
import ca.bc.gov.open.staffnet.biometrics.two.IssuanceToken;
import ca.bc.gov.open.staffnet.biometrics.two.MatchIdentityNameType;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import ca.bc.gov.open.staffnet.controllers.RefreshController;
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
public class RefreshControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testRefreshIdentityWithIdCheck() throws JsonProcessingException {
        var req = new RefreshIdentityWithIdCheck();
        RefreshIdentityWithIdCheckRequest refreshIdentityWithIdCheckRequest =
                new RefreshIdentityWithIdCheckRequest();
        refreshIdentityWithIdCheckRequest.setDid("A");
        refreshIdentityWithIdCheckRequest.setRequesterAccountTypeCode("Business");
        refreshIdentityWithIdCheckRequest.setIndividualID("A");
        refreshIdentityWithIdCheckRequest.setRequestorUserId("A");
        req.setRefreshIdentityWithIdCheckRequest(refreshIdentityWithIdCheckRequest);

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
        refreshIdentityWithIdCheckResponse2.setCode(ResponseCode.SUCCESS);
        refreshIdentityWithIdCheckResponse2.setMessage("A");
        IssuanceToken issuanceToken = new IssuanceToken();
        issuanceToken.setExpiry("A");
        issuanceToken.setIssuanceID("A");
        issuanceToken.setEnrollmentURL("A");
        refreshIdentityWithIdCheckResponse2.setIssuance(issuanceToken);
        soapSvcResp.setRefreshIdentityWithIdCheckResult(refreshIdentityWithIdCheckResponse2);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.RefreshIdentityWithIdCheck
                                        .class)))
                .thenReturn(soapSvcResp);

        RefreshController refreshController =
                new RefreshController(restTemplate, objectMapper, webServiceTemplate);
        var out = refreshController.refreshIdentityWithIdCheck(req);
        Assertions.assertNotNull(out);
    }
}
