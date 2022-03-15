package ca.bc.gov.open.staffnet;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.RefreshController;
import ca.bc.gov.open.staffnet.controllers.SearchController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.net.URI;
import java.time.Instant;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SearchControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testStartSearchForIdentity() throws JsonProcessingException {
        var req = new StartSearchForIdentity();
        var resp = new StartSearchForIdentityResponse();

        StartSearchForIdentityResponse2 two = new StartSearchForIdentityResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setFailureCode("A");
        two.setExpiryDate(Instant.now());
        two.setSearchID("A");
        two.setSearchURL("A");
        resp.setStartSearchForIdentityResponse(two);
        ResponseEntity<StartSearchForIdentityResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<StartSearchForIdentityResponse>>any()))
                .thenReturn(responseEntity);

        SearchController searchController = new SearchController(restTemplate, objectMapper);
        var out = searchController.startSearchForIdentity(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testFinishSearchForIdentity() throws JsonProcessingException {
        var req = new FinishSearchForIdentity();
        var resp = new FinishSearchForIdentityResponse();

        FinishSearchForIdentityResponse2 two = new FinishSearchForIdentityResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setFailureCode("A");
        two.setActive("A");
        two.setStatus("A");
        two.setDID("A");
        resp.setFinishSearchForIdentityResponse(two);
        ResponseEntity<FinishSearchForIdentityResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.GET),
                Mockito.<HttpEntity<String>>any(),
                Mockito.<Class<FinishSearchForIdentityResponse>>any()))
                .thenReturn(responseEntity);

        SearchController searchController = new SearchController(restTemplate, objectMapper);
        var out = searchController.finishSearchForIdentity(req);
        Assertions.assertNotNull(out);
    }
}
