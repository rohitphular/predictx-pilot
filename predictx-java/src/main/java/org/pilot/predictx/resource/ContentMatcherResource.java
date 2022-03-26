package org.pilot.predictx.resource;

import lombok.RequiredArgsConstructor;
import org.pilot.predictx.dto.ContentMatcherRequest;
import org.pilot.predictx.dto.ApiResponse;
import org.pilot.predictx.service.IContentMatcherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/algorithm")
public class ContentMatcherResource {

    private final IContentMatcherService iContentMatcherService;

    @PostMapping("/content-matcher")
    public ResponseEntity<ApiResponse<String>> process(@RequestBody @Valid ContentMatcherRequest contentMatcherRequest) {
        final String result = iContentMatcherService.process(contentMatcherRequest);
        final ApiResponse<String> response = ApiResponse.<String>builder().payload(result).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}