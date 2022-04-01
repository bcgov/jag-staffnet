package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.EnrollmentController;
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
public class EnrollmentControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Autowired private WebServiceTemplate webServiceTemplate;

    @Test
    public void testStartEnrollmentWithIdCheck() throws JsonProcessingException {
        var req = new StartEnrollmentWithIdCheck();

        StartEnrollmentWithIdCheckResponse2 two = new StartEnrollmentWithIdCheckResponse2();
        two.setCode("A");
        two.setMessage("A");
        two.setEnrollmentURL("A");
        two.setFailureCode("A");
        two.setExpiryDate(Instant.now());
        two.setIssuanceId("A");

        ResponseEntity<StartEnrollmentWithIdCheckResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.POST),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<StartEnrollmentWithIdCheckResponse2>>any()))
                .thenReturn(responseEntity);

        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);
        var out = enrollmentController.startEnrollmentWithIdCheck(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void testFinishEnrollmentWithIdCheck() throws JsonProcessingException {
        var req = new FinishEnrollmentWithIdCheck();

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

        ResponseEntity<FinishEnrollmentWithIdCheckResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<FinishEnrollmentWithIdCheckResponse2>>any()))
                .thenReturn(responseEntity);

        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);
        var out = enrollmentController.finishEnrollmentWithIdCheck(req);
        Assertions.assertNotNull(out);
    }
}
