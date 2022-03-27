//package org.pilot.predictx.resource;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.pilot.predictx.ContentAnalyzerApp;
//import org.pilot.predictx.dto.ContentMatcherRequest;
//import org.pilot.predictx.service.IContentMatcherService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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