package org.pilot.content.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.pilot.content.dto.ContentMatcherRequest;
import org.pilot.content.service.IContentMatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ContentMatcherResource.class)
public class ContentMatcherResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IContentMatcherService iContentMatcherService;

    private static ObjectMapper mapper;

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
    }

    @Test
    public void whenBothInputsValid_thenReturn200AndResult() throws Exception {
        final ContentMatcherRequest request = ContentMatcherRequest.builder()
                .input1("Are the kids at home? aaaaa fffff")
                .input2("Yes they are here! aaaaa fffff")
                .build();

        final String expectedResult = "=:aaaaaa/2:eeeee/=:fffff/1:tt/2:rr/=:hh";

        Mockito.when(iContentMatcherService.process(request)).thenReturn(expectedResult);
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post("/algorithm/content-matcher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload", Matchers.equalTo(expectedResult)));
    }

    @Test
    void whenBothInputsNullValue_thenReturns400AndErrorResult() throws Exception {
        final ContentMatcherRequest request = ContentMatcherRequest.builder().build();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post("/algorithm/content-matcher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Input-1 is mandatory")))
                .andExpect(content().string(Matchers.containsString("Input-2 is mandatory")));
    }

    @Test
    void whenBothInputsEmptyValue_thenReturns400AndErrorResult() throws Exception {
        final ContentMatcherRequest request = ContentMatcherRequest.builder().input1("").input2("").build();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post("/algorithm/content-matcher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Matchers.containsString("Input-1 should not be blank")))
                .andExpect(content().string(Matchers.containsString("Input-2 should not be blank")));
    }

}