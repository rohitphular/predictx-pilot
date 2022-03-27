//package org.pilot.predictx.resource;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.pilot.predictx.dto.ContentMatcherRequest;
//import org.pilot.predictx.service.IContentMatcherService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ContentMatcherResource.class)
//class ContentMatcherResourceTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private IContentMatcherService iContentMatcherService;
//
//    private ContentMatcherRequest contentMatcherRequest;
//
//    @Test
//    public void givenStringMatcher_whenBothinputsAreValid_thenRespondsResult() throws Exception{
//        final String mockResponse = "";
//        Mockito.when(iContentMatcherService.process(any())).thenReturn(mockResponse);
//        mockMvc.perform(post("/api/predictx/algorithm/content-matcher").
//                contentType(MediaType.APPLICATION_JSON).
//                content(asJsonString(contentMatcherRequest))).
//                andExpect(status().isOk());
//    }
//
//    public static String asJsonString(final Object obj){
//        try{
//            return new ObjectMapper().writeValueAsString(obj);
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//    }
//}