package ca.bc.gov.open.staffnet;

import static org.mockito.Mockito.when;

import ca.bc.gov.open.staffnet.controllers.ProvisionController;
import ca.bc.gov.open.staffnet.identity_provisioning.one.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collections;
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
        Workers workers = new Workers();
        workers.setDID("A");
        workers.setFirstNm("A");
        workers.setLastNm("A");
        workers.setEmployeeNumber("A");
        one.getWorkers().addAll(Collections.singletonList(workers));

        ResponseEntity<GetProvisionedWorkerResponse> responseEntity =
                new ResponseEntity<>(one, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetProvisionedWorkerResponse>>any()))
                .thenReturn(responseEntity);

        ProvisionController provisionController =
                new ProvisionController(restTemplate, objectMapper);
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
        two.setEnrollmentDtm("A");
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

        ResponseEntity<GetWorkerProvisioningQueueItemResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<GetWorkerProvisioningQueueItemResponse2>>any()))
                .thenReturn(responseEntity);

        ProvisionController provisionController =
                new ProvisionController(restTemplate, objectMapper);
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

        ResponseEntity<SetWorkerProvisioningStatusResponse2> responseEntity =
                new ResponseEntity<>(two, HttpStatus.OK);

        // Set up to mock ords response
        when(restTemplate.exchange(
                        Mockito.any(URI.class),
                        Mockito.eq(HttpMethod.PUT),
                        Mockito.<HttpEntity<String>>any(),
                        Mockito.<Class<SetWorkerProvisioningStatusResponse2>>any()))
                .thenReturn(responseEntity);

        ProvisionController provisionController =
                new ProvisionController(restTemplate, objectMapper);
        var out = provisionController.setWorkerProvisioningStatus(req);
        Assertions.assertNotNull(out);
    }
}
