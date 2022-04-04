package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.RefreshController;
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
public class RefreshControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Autowired private WebServiceTemplate webServiceTemplate;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testRefreshIdentityWithIdCheck() throws JsonProcessingException {
        var req = new RefreshIdentityWithIdCheck();

        RefreshIdentityWithIdCheckResponse2 two = new RefreshIdentityWithIdCheckResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setEnrollmentURL("A");
        two.setFailureCode("A");
        two.setExpiryDate(Instant.now());
        two.setIssuanceId("A");

        ResponseEntity<RefreshIdentityWithIdCheckResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<RefreshIdentityWithIdCheckResponse2>>any()))
                .thenReturn(responseEntity);

        RefreshController refreshController = new RefreshController(restTemplate, objectMapper, webServiceTemplate);
        var out = refreshController.refreshIdentityWithIdCheck(req);
        Assertions.assertNotNull(out);
    }
}
