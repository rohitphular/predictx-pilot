package org.pilot.predictx.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pilot.predictx.ContentAnalyserApp;
import org.pilot.predictx.dto.ApiResponse;
import org.pilot.predictx.dto.ContentMatcherRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ContentAnalyserApp.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContentMatcherResourceIT {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final HttpHeaders headers = new HttpHeaders();

    @Test
    public void whenBothInputsAreValid_thenExpect200ResponseAndOutput() {
        final ContentMatcherRequest request = ContentMatcherRequest.builder()
                .input1("Are the kids at home? aaaaa fffff")
                .input2("Yes they are here! aaaaa fffff")
                .build();

        HttpEntity<ContentMatcherRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/predictx/algorithm/content-matcher"),
                HttpMethod.POST, entity, String.class);

        final String resultExpected = "=:aaaaaa/2:eeeee/=:fffff/1:tt/2:rr/=:hh";
        final ApiResponse<String> expected = ApiResponse.<String>builder().payload(resultExpected).build();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(asJsonString(expected), response.getBody());
    }

    @Test
    public void whenBothInputsAreNull_thenExpect400Response() {
        final ContentMatcherRequest request = ContentMatcherRequest.builder().build();

        HttpEntity<ContentMatcherRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/predictx/algorithm/content-matcher"),
                HttpMethod.POST, entity, String.class);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void whenBothInputsAreEmpty_thenExpect400Response() {
        final ContentMatcherRequest request = ContentMatcherRequest.builder()
                .input1("")
                .input2("")
                .build();

        HttpEntity<ContentMatcherRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/predictx/algorithm/content-matcher"),
                HttpMethod.POST, entity, String.class);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void whenOneInputIsEmpty_thenExpect400Response() {
        final ContentMatcherRequest request = ContentMatcherRequest.builder()
                .input1("Are the kids at home? aaaaa fffff")
                .input2("")
                .build();

        HttpEntity<ContentMatcherRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/predictx/algorithm/content-matcher"),
                HttpMethod.POST, entity, String.class);

        assertEquals(400, response.getStatusCodeValue());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}