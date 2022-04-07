package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.three.ActiveCodeResponse;
import ca.bc.gov.open.staffnet.biometrics.three.ResponseCode;
import ca.bc.gov.open.staffnet.biometrics.three.SearchStatusCode;
import ca.bc.gov.open.staffnet.biometrics.three.SearchToken;
import ca.bc.gov.open.staffnet.controllers.SearchController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SearchControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testStartSearchForIdentity() throws JsonProcessingException {
        var req = new StartSearchForIdentity();
        StartSearchForIdentityRequest startSearchForIdentityRequest =
                new StartSearchForIdentityRequest();
        startSearchForIdentityRequest.setActiveOnly("Y");
        startSearchForIdentityRequest.setRequestorUserId("A");
        startSearchForIdentityRequest.setRequestorAccountTypeCode("Business");
        req.setStartSearchForIdentityRequest(startSearchForIdentityRequest);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityResponse();
        ca.bc.gov.open.staffnet.biometrics.three.StartSearchForIdentityResponse
                startSearchForIdentityResponse =
                        new ca.bc.gov.open.staffnet.biometrics.three
                                .StartSearchForIdentityResponse();
        startSearchForIdentityResponse.setMessage("A");
        startSearchForIdentityResponse.setCode(ResponseCode.SUCCESS);
        SearchToken searchToken = new SearchToken();
        searchToken.setSearchID("A");
        searchToken.setSearchURL("A");
        searchToken.setExpiry(Instant.now());
        startSearchForIdentityResponse.setSearch(searchToken);
        soapSvcResp.setStartSearchForIdentityResult(startSearchForIdentityResponse);

        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentity
                                        .class)))
                .thenReturn(soapSvcResp);

        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);
        var out = searchController.startSearchForIdentity(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testFinishSearchForIdentity() throws JsonProcessingException {
        var req = new FinishSearchForIdentity();
        FinishSearchForIdentityRequest finishSearchForIdentityRequest =
                new FinishSearchForIdentityRequest();
        finishSearchForIdentityRequest.setSearchID("A");
        finishSearchForIdentityRequest.setRequestorUserId("A");
        finishSearchForIdentityRequest.setRequestorAccountTypeCode("Business");
        finishSearchForIdentityRequest.setRequesterUserGuid("A");
        req.setFinishSearchForIdentityRequest(finishSearchForIdentityRequest);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityResponse();
        ca.bc.gov.open.staffnet.biometrics.three.FinishSearchForIdentityResponse
                finishSearchForIdentityResponse =
                        new ca.bc.gov.open.staffnet.biometrics.three
                                .FinishSearchForIdentityResponse();
        finishSearchForIdentityResponse.setDID("A");
        finishSearchForIdentityResponse.setActive(ActiveCodeResponse.Y);
        finishSearchForIdentityResponse.setCode(ResponseCode.SUCCESS);
        finishSearchForIdentityResponse.setMessage("A");
        finishSearchForIdentityResponse.setStatus(SearchStatusCode.FOUND);
        soapSvcResp.setFinishSearchForIdentityResult(finishSearchForIdentityResponse);
        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentity
                                        .class)))
                .thenReturn(soapSvcResp);

        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);
        var out = searchController.finishSearchForIdentity(req);
        Assertions.assertNotNull(out);
    }
}
