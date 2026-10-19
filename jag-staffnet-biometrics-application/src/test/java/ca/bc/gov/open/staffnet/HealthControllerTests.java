package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.GetHealth;
import ca.bc.gov.open.staffnet.biometrics.one.GetHealthResponse;
import ca.bc.gov.open.staffnet.biometrics.one.GetPing;
import ca.bc.gov.open.staffnet.biometrics.one.GetPingResponse;
import ca.bc.gov.open.staffnet.controllers.HealthController;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getPingTest() throws JacksonException {
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
    public void getHealthTest() throws JacksonException {
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
