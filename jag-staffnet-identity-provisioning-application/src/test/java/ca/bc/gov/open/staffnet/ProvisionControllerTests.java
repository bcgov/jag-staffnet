package ca.bc.gov.open.staffnet;

import ca.bc.gov.open.staffnet.controllers.HealthController;
import ca.bc.gov.open.staffnet.controllers.ProvisionController;
import ca.bc.gov.open.staffnet.identity_provisioning.one.*;
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
public class ProvisionControllerTests {
    @Autowired private ObjectMapper objectMapper;

    @Mock private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getProvisionedWorkersTest() throws JsonProcessingException {
        var req = new GetProvisionedWorkers();
        var resp = new GetProvisionedWorkersResponse();

        GetProvisionedWorkerResponse one = new GetProvisionedWorkerResponse();
        one.setRecordCount("A");
        one.setResponseCd("A");
        one.setResponseMessage("A");
        one.setWorkers(null);
        resp.setGetProvisionedWorkerResponse(one);

        ResponseEntity<GetProvisionedWorkersResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetProvisionedWorkersResponse>>any()))
                .thenReturn(responseEntity);

        ProvisionController provisionController = new ProvisionController(restTemplate, objectMapper);
        var out = provisionController.getProvisionedWorkers(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void GetWorkerProvisioningQueueItemTest() throws JsonProcessingException {
        var req = new GetWorkerProvisioningQueueItem();
        var resp = new GetWorkerProvisioningQueueItemResponse();

        GetWorkerProvisioningQueueItemResponse2 two = new GetWorkerProvisioningQueueItemResponse2();
        two.setEmployeeNo("A");
        two.setEventId("A");
        two.setEnrollmentDtm(Instant.now());
        two.setResponseCd("A");
        two.setResponseMessage("A");
        two.setFirstNm("A");
        two.setLastNm("A");
        two.setJobTitle("A");
        two.setOperationMode("A");
        two.setBiometricTemplateURL("A");
        two.setPhoto(null);
        two.setProvisioningTargetId("A");
        two.setWorkerDID("A");
        resp.setGetWorkerProvisioningQueueItemResponse(two);

        ResponseEntity<GetWorkerProvisioningQueueItemResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.GET),
                Mockito.<HttpEntity<String>>any(),
                Mockito.<Class<GetWorkerProvisioningQueueItemResponse>>any()))
                .thenReturn(responseEntity);

        ProvisionController provisionController = new ProvisionController(restTemplate, objectMapper);
        var out = provisionController.getWorkerProvisioningQueueItem(req);
        Assertions.assertNotNull(out);
    }

    @Test
    public void SetWorkerProvisioningStatusTest() throws JsonProcessingException {
        var req = new SetWorkerProvisioningStatus();
        var resp = new SetWorkerProvisioningStatusResponse();

        SetWorkerProvisioningStatusResponse2 two = new SetWorkerProvisioningStatusResponse2();
        two.setResponseCd("A");
        two.setResponseMessage("A");
        resp.setSetWorkerProvisioningStatusResponse(two);

        ResponseEntity<SetWorkerProvisioningStatusResponse> responseEntity = new ResponseEntity<>(resp, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.PUT),
                Mockito.<HttpEntity<String>>any(),
                Mockito.<Class<SetWorkerProvisioningStatusResponse>>any()))
                .thenReturn(responseEntity);

        ProvisionController provisionController = new ProvisionController(restTemplate, objectMapper);
        var out = provisionController.setWorkerProvisioningStatus(req);
        Assertions.assertNotNull(out);
    }
}
