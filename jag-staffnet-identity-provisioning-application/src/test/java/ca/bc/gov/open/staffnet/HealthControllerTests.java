package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.controllers.HealthController;
import ca.bc.gov.open.staffnet.identity_provisioning.one.GetHealth;
import ca.bc.gov.open.staffnet.identity_provisioning.one.GetHealthResponse;
import ca.bc.gov.open.staffnet.identity_provisioning.one.GetPing;
import ca.bc.gov.open.staffnet.identity_provisioning.one.GetPingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getPingTest() throws JsonProcessingException {
        var req = new GetPing();
        var resp = new GetPingResponse();

        resp.setStatus("A");
        ResponseEntity<GetPingResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetPingResponse>>any()))
                .thenReturn(responseEntity);

        HealthController healthController = new HealthController(restTemplate, objectMapper);
        var out = healthController.getPing(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void getHealthTest() throws JsonProcessingException {
        var req = new GetHealth();
        var resp = new GetHealthResponse();

        resp.setAppid("A");
        resp.setMethod("A");
        resp.setStatus("A");
        resp.setHost("A");
        resp.setInstance("A");
        resp.setVersion("A");
        resp.setCompatibility("A");
        ResponseEntity<GetHealthResponse> responseEntity =
                new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetHealthResponse>>any()))
                .thenReturn(responseEntity);

        HealthController healthController = new HealthController(restTemplate, objectMapper);
        var out = healthController.getHealth(req);
        Assertions.assertNotNull(out);
    }
}
