package org.pilot.predictx.integration;

import org.junit.jupiter.api.Test;
import org.pilot.predictx.ContentAnalyzerApp;
import org.pilot.predictx.dto.ContentMatcherRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {ContentAnalyzerApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContentMatcherResourceIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void request1() {
        final ContentMatcherRequest request = ContentMatcherRequest.builder()
                .input1("Are the kids at home? aaaaa fffff")
                .input2("Yes they are here! aaaaa fffff")
                .build();

        HttpEntity<ContentMatcherRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/predictx/algorithm/content-matcher"),
                HttpMethod.POST, entity, String.class);

        assertEquals(200, response.getStatusCodeValue());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}