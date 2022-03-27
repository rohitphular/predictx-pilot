package org.pilot.predictx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pilot.predictx.dto.ContentMatcherRequest;
import org.pilot.predictx.service.impl.ContentMatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ContentMatcherServiceTest {

    @Autowired
    @InjectMocks
    private ContentMatcherService contentMatcherService;

    @Test
    public void givenProcess_whenValidInput_thenSuccessOutput_1() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1("my&friend&Paul has heavy hats! &")
                .input2("my friend John has many many friends &")
                .build();

        final String expected = "2:nnnnn/1:aaaa/1:hhh/2:mmm/2:yyy/2:dd/2:ff/2:ii/2:rr/=:ee/=:ss";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenValidInput_thenSuccessOutput_2() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1("mmmmm m nnnnn y&friend&Paul has heavy hats! &")
                .input2("my frie n d Joh n has ma n y ma n y frie n ds n&")
                .build();

        final String expected = "1:mmmmmm/=:nnnnnn/1:aaaa/1:hhh/2:yyy/2:dd/2:ff/2:ii/2:rr/=:ee/=:ss";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenValidInput_thenSuccessOutput_3() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1("Are the kids at home? aaaaa fffff")
                .input2("Yes they are here! aaaaa fffff")
                .build();

        final String expected = "=:aaaaaa/2:eeeee/=:fffff/1:tt/2:rr/=:hh";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenBothInputSame_thenSuccessOutput() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1("aaaaa bbbbb")
                .input2("aaaaa bbbbb")
                .build();

        final String expected = "=:aaaaa/=:bbbbb";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenInputLetterSameCount_thenSuccessOutput() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1("hi")
                .input2("hi")
                .build();

        final String expected = "No Result";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenBothInputAreEmpty_thenNoResultOutput() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1("")
                .input2("")
                .build();

        final String expected = "No Result";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenBothInputAreNull_thenNoResultOutput() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input1(null)
                .input2(null)
                .build();

        final String expected = "No Result";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

    @Test
    public void givenProcess_whenOneInputIsEmpty_thenNoResultOutput() {
        ContentMatcherRequest contentMatcherRequest = ContentMatcherRequest.builder()
                .input2("some content")
                .build();

        final String expected = "2:tt/2:ee/2:nn/2:oo";
        final String result = contentMatcherService.process(contentMatcherRequest);

        assertEquals(expected, result);
    }

}