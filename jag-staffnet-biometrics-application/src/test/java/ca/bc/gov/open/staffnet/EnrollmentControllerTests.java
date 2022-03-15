package ca.bc.gov.open.staffnet;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.BiometricController;
import ca.bc.gov.open.staffnet.controllers.EnrollmentController;
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
public class EnrollmentControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testStartEnrollmentWithIdCheck() throws JsonProcessingException {
        var req = new StartEnrollmentWithIdCheck();
        var resp = new StartEnrollmentWithIdCheckResponse();

        StartEnrollmentWithIdCheckResponse2 two = new StartEnrollmentWithIdCheckResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setEnrollmentURL("A");
        two.setFailureCode("A");
        two.setExpiryDate(Instant.now());
        two.setIssuanceId("A");
        resp.setStartEnrollmentWithIdCheckResponse(two);
        ResponseEntity<StartEnrollmentWithIdCheckResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<StartEnrollmentWithIdCheckResponse>>any()))
                .thenReturn(responseEntity);

        EnrollmentController enrollmentController = new EnrollmentController(restTemplate, objectMapper);
        var out = enrollmentController.startEnrollmentWithIdCheck(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testFinishEnrollmentWithIdCheck() throws JsonProcessingException {
        var req = new FinishEnrollmentWithIdCheck();
        var resp = new FinishEnrollmentWithIdCheckResponse();

        FinishEnrollmentWithIdCheckResponse2 two = new FinishEnrollmentWithIdCheckResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setPhoto(null);
        two.setBiometricTemplateUrl("A");
        two.setFailureCode("A");
        two.setPhotoTakenDate(Instant.now());
        two.setGivenNames("A");
        two.setLastName("A");
        two.setImageSetSuccessYN("A");
        two.setDid("A");
        two.setDateOfBirth("A");
        resp.setFinishEnrollmentWithIdCheckResponse(two);
        ResponseEntity<FinishEnrollmentWithIdCheckResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.PUT),
                Mockito.<HttpEntity<String>>any(),
                Mockito.<Class<FinishEnrollmentWithIdCheckResponse>>any()))
                .thenReturn(responseEntity);

        EnrollmentController enrollmentController = new EnrollmentController(restTemplate, objectMapper);
        var out = enrollmentController.finishEnrollmentWithIdCheck(req);
        Assertions.assertNotNull(out);
    }
}
