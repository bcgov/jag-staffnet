package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.SearchController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Instant;
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
public class SearchControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Autowired private WebServiceTemplate webServiceTemplate;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testStartSearchForIdentity() throws JsonProcessingException {
        var req = new StartSearchForIdentity();

        StartSearchForIdentityResponse2 two = new StartSearchForIdentityResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setFailureCode("A");
        two.setExpiryDate(Instant.now());
        two.setSearchID("A");
        two.setSearchURL("A");

        ResponseEntity<StartSearchForIdentityResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<StartSearchForIdentityResponse2>>any()))
                .thenReturn(responseEntity);

        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);
        var out = searchController.startSearchForIdentity(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testFinishSearchForIdentity() throws JsonProcessingException {
        var req = new FinishSearchForIdentity();

        FinishSearchForIdentityResponse2 two = new FinishSearchForIdentityResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setFailureCode("A");
        two.setActive("A");
        two.setStatus("A");
        two.setDID("A");

        ResponseEntity<FinishSearchForIdentityResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<FinishSearchForIdentityResponse2>>any()))
                .thenReturn(responseEntity);

        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);
        var out = searchController.finishSearchForIdentity(req);
        Assertions.assertNotNull(out);
    }
}
