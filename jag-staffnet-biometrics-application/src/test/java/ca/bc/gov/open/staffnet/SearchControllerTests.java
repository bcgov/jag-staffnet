package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.two.ActiveCodeResponse;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import ca.bc.gov.open.staffnet.biometrics.two.SearchStatusCode;
import ca.bc.gov.open.staffnet.biometrics.two.SearchToken;
import ca.bc.gov.open.staffnet.controllers.SearchController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        ca.bc.gov.open.staffnet.biometrics.two.StartSearchForIdentityResponse2
                startSearchForIdentityResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .StartSearchForIdentityResponse2();
        startSearchForIdentityResponse2.setMessage("A");
        startSearchForIdentityResponse2.setCode(ResponseCode.SUCCESS);
        SearchToken searchToken = new SearchToken();
        searchToken.setSearchID("A");
        searchToken.setSearchURL("A");
        searchToken.setExpiry("A");
        startSearchForIdentityResponse2.setSearch(searchToken);
        soapSvcResp.setStartSearchForIdentityResult(startSearchForIdentityResponse2);

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
        ca.bc.gov.open.staffnet.biometrics.two.FinishSearchForIdentityResponse2
                finishSearchForIdentityResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .FinishSearchForIdentityResponse2();
        finishSearchForIdentityResponse2.setDID("A");
        finishSearchForIdentityResponse2.setActive(ActiveCodeResponse.Y);
        finishSearchForIdentityResponse2.setCode(ResponseCode.SUCCESS);
        finishSearchForIdentityResponse2.setMessage("A");
        finishSearchForIdentityResponse2.setStatus(SearchStatusCode.FOUND);
        soapSvcResp.setFinishSearchForIdentityResult(finishSearchForIdentityResponse2);
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
