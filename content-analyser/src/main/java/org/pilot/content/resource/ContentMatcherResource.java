package org.pilot.content.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pilot.content.dto.ContentMatcherRequest;
import org.pilot.content.dto.ApiResponse;
import org.pilot.content.service.IContentMatcherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/algorithm")
public class ContentMatcherResource {

    private final IContentMatcherService iContentMatcherService;

    @PostMapping("/content-matcher")
    public ResponseEntity<ApiResponse<String>> process(@RequestBody @Valid ContentMatcherRequest contentMatcherRequest) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Request received for content matcher algorithm");
        final String result = iContentMatcherService.process(contentMatcherRequest);
        final ApiResponse<String> response = ApiResponse.<String>builder().payload(result).build();
        log.info("Request processed successfully for content matcher algorithm");

        stopWatch.stop();
        log.debug("Performance | Content matcher algorithm | {}ms", stopWatch.getTotalTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}