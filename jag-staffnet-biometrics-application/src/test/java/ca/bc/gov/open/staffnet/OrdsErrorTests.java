package ca.bc.gov.open.staffnet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.biometrics.two.ResponseCode;
import ca.bc.gov.open.staffnet.controllers.*;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrdsErrorTests {
    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Mock private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testPingOrdsFail() {
        HealthController healthController = new HealthController(restTemplate, objectMapper);

        Assertions.assertThrows(ORDSException.class, () -> healthController.getPing(new GetPing()));
    }

    @Test
    public void testHealthOrdsFail() {
        HealthController healthController = new HealthController(restTemplate, objectMapper);

        Assertions.assertThrows(
                ORDSException.class, () -> healthController.getHealth(new GetHealth()));
    }

    @Test
    public void testBiometricReconciliationOrdsFail() {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () -> biometricController.biometricReconciliation(new BiometricReconciliation()));
    }

    @Test
    public void testStartEnrollmentWithIdCheckOrdsFail() {
        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        enrollmentController.startEnrollmentWithIdCheck(
                                new StartEnrollmentWithIdCheck()));
    }

    @Test
    public void testFinishEnrollmentWithIdCheckOrdsFail() {
        EnrollmentController enrollmentController =
                new EnrollmentController(restTemplate, objectMapper, webServiceTemplate);

        // Set up to mock soap service response
        ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse soapSvcResp =
                new ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse();
        ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheckResponse2
                finishEnrollmentWithIdCheckResponse2 =
                        new ca.bc.gov.open.staffnet.biometrics.two
                                .FinishEnrollmentWithIdCheckResponse2();
        finishEnrollmentWithIdCheckResponse2.setMessage("A");
        finishEnrollmentWithIdCheckResponse2.setCode(ResponseCode.SUCCESS);
        finishEnrollmentWithIdCheckResponse2.setDid("A");
        finishEnrollmentWithIdCheckResponse2.setPhoto(new byte[0]);
        finishEnrollmentWithIdCheckResponse2.setDateOfBirth("A");
        finishEnrollmentWithIdCheckResponse2.setBiometricTemplateUrl("A");
        finishEnrollmentWithIdCheckResponse2.setGivenNames("A");
        finishEnrollmentWithIdCheckResponse2.setLastName("A");
        finishEnrollmentWithIdCheckResponse2.setPhotoTakenDate(Instant.now());
        soapSvcResp.setFinishEnrollmentWithIdCheckResult(finishEnrollmentWithIdCheckResponse2);

        when(webServiceTemplate.marshalSendAndReceive(
                        anyString(),
                        Mockito.any(
                                ca.bc.gov.open.staffnet.biometrics.two.FinishEnrollmentWithIdCheck
                                        .class)))
                .thenReturn(soapSvcResp);

        //        // Set up to mock ords response
        //        when(restTemplate.exchange(
        //                        Mockito.any(URI.class),
        //                        Mockito.eq(HttpMethod.PUT),
        //                        Mockito.<HttpEntity<String>>any(),
        //                        Mockito.<Class<WorkerImageSetResponse>>any()))
        //                .thenThrow(new ORDSException());

        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        enrollmentController.finishEnrollmentWithIdCheck(
                                new FinishEnrollmentWithIdCheck()));
    }

    @Test
    public void testRefreshIdentityWithIdCheckOrdsFail() {
        RefreshController refreshController =
                new RefreshController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        refreshController.refreshIdentityWithIdCheck(
                                new RefreshIdentityWithIdCheck()));
    }

    @Test
    public void securityTestFail_Then401() throws Exception {
        var response =
                mockMvc.perform(post("/ws").contentType(MediaType.TEXT_XML))
                        .andExpect(status().is4xxClientError())
                        .andReturn();
        Assertions.assertEquals(
                HttpStatus.UNAUTHORIZED.value(), response.getResponse().getStatus());
    }
}
