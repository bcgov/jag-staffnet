package ca.bc.gov.open.staffnet.test.controllers;

import ca.bc.gov.open.staffnet.test.services.TestService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/tests")
@Slf4j
public class TestController {
    @Value("${staffnet.host}")
    private String host = "https://127.0.0.1/";

    private final RestTemplate restTemplate = new RestTemplate();
    private TestService testService;


    @Autowired
    public TestController(TestService testService) throws IOException {

        this.testService = testService;
        this.testService.setAuthentication("StaffnetIdentity-soapui-project.xml");

    }

    @GetMapping(value = "/all")
    public ResponseEntity runAllTests() throws IOException {
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(host + "tests/init");
        try {
            HttpEntity<Map<String, String>> resp =
                    restTemplate.exchange(
                            builder.build().encode().toUri(),
                            HttpMethod.POST,
                            new HttpEntity<>(new HttpHeaders()),
                            new ParameterizedTypeReference<>(){});

         if(resp.getBody().get("status").equals("fail")) {
             return new ResponseEntity<String>("{\"status\": \"table not updated\"}", responseHeaders, HttpStatus.OK);
         }
        } catch (Exception ex) {

        }

        File f = testService.runAllTests();
        if (f != null && f.exists()) {
            InputStream inputStream = new FileInputStream(f.getAbsolutePath());
            //      This is not great streaming would be better but small files should be ok
            byte[] out = org.apache.commons.io.IOUtils.toByteArray(inputStream);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=" + f.getName());
            responseHeaders.add("Content-Type", "application/zip");
            f.delete();
            return new ResponseEntity<byte[]>(out, responseHeaders, HttpStatus.OK);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Type", "application/json");
            return new ResponseEntity<String>("{\"status\": \"All tests passed\"}", responseHeaders, HttpStatus.OK);
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "ping";
    }
}
