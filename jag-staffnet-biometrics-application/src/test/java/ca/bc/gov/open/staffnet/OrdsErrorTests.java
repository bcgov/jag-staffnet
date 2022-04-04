package ca.bc.gov.open.staffnet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.open.staffnet.biometrics.one.*;
import ca.bc.gov.open.staffnet.controllers.*;
import ca.bc.gov.open.staffnet.exceptions.ORDSException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired private WebServiceTemplate webServiceTemplate;

    @Mock private RestTemplate restTemplate;

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
    public void testBiometricReconciliationSoapServiceFail() {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () -> biometricController.biometricReconciliation(new BiometricReconciliation()));
    }

    @Test
    public void testDeactivateBiometricCredentialByDIDSoapServiceFail() {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        biometricController.deactivateBiometricCredentialByDID(
                                new DeactivateBiometricCredentialByDID()));
    }

    @Test
    public void testDestroyBiometricCredentialByDIDSoapServiceFail() {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        biometricController.destroyBiometricCredentialByDID(
                                new DestroyBiometricCredentialByDID()));
    }

    @Test
    public void testReactivateBiometricCredentialByDIDSoapServiceFail() {
        BiometricController biometricController =
                new BiometricController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () ->
                        biometricController.reactivateBiometricCredentialByDID(
                                new ReactivateBiometricCredentialByDID()));
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
    public void testStartSearchForIdentityOrdsFail() {
        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () -> searchController.startSearchForIdentity(new StartSearchForIdentity()));
    }

    @Test
    public void testFinishSearchForIdentityOrdsFail() {
        SearchController searchController =
                new SearchController(restTemplate, objectMapper, webServiceTemplate);

        Assertions.assertThrows(
                ORDSException.class,
                () -> searchController.finishSearchForIdentity(new FinishSearchForIdentity()));
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
