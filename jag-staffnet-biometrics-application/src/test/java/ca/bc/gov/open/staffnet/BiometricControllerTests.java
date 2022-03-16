package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.BiometricController;
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
public class BiometricControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testBiometricReconciliation() throws JsonProcessingException {
        var req = new BiometricReconciliation();
        var resp = new BiometricReconciliationResponse();

        BiometricReconciliationResponse2 two = new BiometricReconciliationResponse2();
        two.setResponseCd("A");
        two.setResponseTxt("A");
        resp.setBiometricReconciliationResponse(two);
        ResponseEntity<BiometricReconciliationResponse> responseEntity =
                new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<BiometricReconciliationResponse>>any()))
                .thenReturn(responseEntity);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper);
        var out = biometricController.biometricReconciliation(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testDeactivateBiometricCredentialByDID() throws JsonProcessingException {
        var req = new DeactivateBiometricCredentialByDID();
        var resp = new DeactivateBiometricCredentialByDIDResponse();

        DeactivateBiometricCredentialByDIDResponse2 two =
                new DeactivateBiometricCredentialByDIDResponse2();
        two.setCode("A");
        two.setFailureCode("A");
        two.setMessage("A");
        resp.setDeactivateBiometricCredentialByDIDResponse(two);
        ResponseEntity<DeactivateBiometricCredentialByDIDResponse> responseEntity =
                new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<DeactivateBiometricCredentialByDIDResponse>>any()))
                .thenReturn(responseEntity);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper);
        var out = biometricController.deactivateBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testDestroyBiometricCredentialByDID() throws JsonProcessingException {
        var req = new DestroyBiometricCredentialByDID();
        var resp = new DestroyBiometricCredentialByDIDResponse();

        DestroyBiometricCredentialByDIDResponse2 two =
                new DestroyBiometricCredentialByDIDResponse2();
        two.setCode("A");
        two.setFailureCode("A");
        two.setMessage("A");
        resp.setDestroyBiometricCredentialByDIDResponse(two);
        ResponseEntity<DestroyBiometricCredentialByDIDResponse> responseEntity =
                new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.DELETE),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<DestroyBiometricCredentialByDIDResponse>>any()))
                .thenReturn(responseEntity);

        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper);
        var out = biometricController.destroyBiometricCredentialByDID(req);
        Assertions.assertNotNull(out);
    }
}
