package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.BiometricController;
import ca.bc.gov.open.staffnet.models.GetEnrolledWorkersOutput;
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
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BiometricControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Autowired private WebServiceTemplate webServiceTemplate;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testBiometricReconciliation() throws JsonProcessingException {
        var req = new BiometricReconciliation();

        BiometricReconciliationResponse2 two = new BiometricReconciliationResponse2();
        two.setResponseCd("A");
        two.setResponseTxt("A");

        ResponseEntity<BiometricReconciliationResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<BiometricReconciliationResponse2>>any()))
                .thenReturn(responseEntity);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.biometricReconciliation(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testDeactivateBiometricCredentialByDID() throws Exception {
        var req = new DeactivateBiometricCredentialByDID();

        DeactivateBiometricCredentialByDIDResponse2 two =
                new DeactivateBiometricCredentialByDIDResponse2();
        two.setCode("A");
        two.setFailureCode("A");
        two.setMessage("A");

        ResponseEntity<DeactivateBiometricCredentialByDIDResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<DeactivateBiometricCredentialByDIDResponse2>>any()))
                .thenReturn(responseEntity);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.deactivateBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testDestroyBiometricCredentialByDID() throws JsonProcessingException {
        var req = new DestroyBiometricCredentialByDID();

        DestroyBiometricCredentialByDIDResponse2 two =
                new DestroyBiometricCredentialByDIDResponse2();
        two.setCode("A");
        two.setFailureCode("A");
        two.setMessage("A");

        ResponseEntity<DestroyBiometricCredentialByDIDResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.DELETE),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<DestroyBiometricCredentialByDIDResponse2>>any()))
                .thenReturn(responseEntity);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);
        var out = biometricController.destroyBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }
}
