package ca.bc.gov.open.staffnet;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.EnrollmentController;
import ca.bc.gov.open.staffnet.controllers.RefreshController;
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
public class RefreshControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testRefreshIdentityWithIdCheck() throws JsonProcessingException {
        var req = new RefreshIdentityWithIdCheck();
        var resp = new RefreshIdentityWithIdCheckResponse();

        RefreshIdentityWithIdCheckResponse2 two = new RefreshIdentityWithIdCheckResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setEnrollmentURL("A");
        two.setFailureCode("A");
        two.setExpiryDate(Instant.now());
        two.setIssuanceId("A");
        resp.setRefreshIdentityWithIdCheckResponse(two);
        ResponseEntity<RefreshIdentityWithIdCheckResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<RefreshIdentityWithIdCheckResponse>>any()))
                .thenReturn(responseEntity);

        RefreshController refreshController = new RefreshController(restTemplate, objectMapper);
        var out = refreshController.refreshIdentityWithIdCheck(req);
        Assertions.assertNotNull(out);
    }
}
