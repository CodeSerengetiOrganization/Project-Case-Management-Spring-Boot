package com.mytech.casemanagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.config.JacksonConfig;
import com.mytech.casemanagement.entity.ErrorResponse;
import com.mytech.casemanagement.service.CaseActionHandlerService;
import com.mytech.casemanagement.service.CaseServiceNew;
import com.mytech.casemanagement.service.CaseValidationService;
import com.mytech.casemanagement.validator.RequestValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(CaseController.class)
public class CaseControllerTests {

/*    @InjectMocks
    private CaseController caseController;*/

    @MockBean
    private CaseActionHandlerService caseActionHandlerService;

    @MockBean
    private CaseServiceNew caseServiceNew;

    @MockBean
    private RequestValidator requestValidator;

    @MockBean
    private CaseValidationService caseValidationService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CaseController caseController; // Autowire the controller

    @Autowired ObjectMapper objectMapper;

    private String requestStr="{\n" +
            "    \"action\":\"create\",\n" +
            "    \"payload\": {\n" +
            "        \"caseIds\": 101,\n" +
            "        \"caseStatus\": \"PendingDocument\",\n" +
            "        \"caseType\": \"Fraud\",\n" +
            "        \"createdBy\": \"John Stark\",\n" +
            "        \"createDate\": \"2024-08-07T18:59:35\",\n" +
            "        \"modifiedDate\": \"2024-08-08T18:59:35\",\n" +
            "        \"pendingReviewDate\": \"2024-08-09T18:59:35\",\n" +
            "        \"note\": \"using RequestObject mapping.\"\n" +
            "    }\n" +
            "}";

    @Test
    public void happyPathShouldReturnResponseEntity(){

        ResponseEntity<?> mockSuccessfulResponseEntity=ResponseEntity.ok().body("Successful Response");
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");
        when(caseActionHandlerService.invokeActionHandlerStrRequest(anyString(), anyString(), anyString(), anyString()))
                .thenReturn((ResponseEntity)mockSuccessfulResponseEntity);
        ResponseEntity<?> response = caseController.createCaseNew2("dar", "create", requestStr);
        Assertions.assertTrue(response.getStatusCode().value() == 200);
        String body = (String)response.getBody();
        System.out.println("body: "+body);
        Assertions.assertTrue(body.contains("Successful Response"));
    }
    @Test
    public void testGetCaseWithCaseIdNotExistingShouldReturnResponseEntityWithErrorMessage() throws Exception {
        int nonExistingCaseId=98761234;
        mockMvc.perform(get("/api/cases/v3/"+nonExistingCaseId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    JsonNode jsonNode = objectMapper.readTree(content);
                    String errorCode = jsonNode.get("errorCode").asText();
                    String status = jsonNode.get("status").asText();
                    String message = jsonNode.get("message").asText();
                    Assertions.assertEquals("CASE_RESOURCE_NOT_FOUND",errorCode);
                    Assertions.assertEquals("400",status);
                    Assertions.assertTrue(message.contains("Case not found for ID: 98761234,workflow: mockedWorkflow"));
                });
    }
/*
* A scenario with input caseId is string rather than an integer.
* It is a must to use mockMvc to perform the API request, bcz a string type caseId will trigger MethodArgumentTypeMismatchException,
* which is thrown by Spring framework BEFORE code goes into the getCaseByCaseId() method.
* */
    @Test
    public void testGetCaseWithInvalidCaseIdShouldHandledByControllerAdvice() throws Exception {
        String invalidCaseId = "abc123";
        mockMvc.perform(get("/api/cases/v3/"+invalidCaseId)) // Replace "/v3/{caseId}" with your endpoint
                .andExpect(status().isBadRequest()) // Expecting 400 Bad Request
                .andExpect(result -> {
                    // Parse JSON response
//                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());

                    //Extract error message;
                    String errorCode = jsonNode.get("errorCode").asText();
                    String status = jsonNode.get("status").asText();
                    String message = jsonNode.get("message").asText();
                    Assertions.assertEquals("INVALID_CASE_ID",errorCode);
                    Assertions.assertEquals("400",status);
                    Assertions.assertTrue(message.contains("Invalid value type of"));
                });
    }
    /*
    * A scenario with input caseId is a negative value
    * */
    @Test
    public void testGetCaseWithNegativeCaseIdShouldReturnResponseEntityWithErrorMessage() throws Exception {
        int negativeCaseId=-1;
        mockMvc.perform(get("/api/cases/v3/"+negativeCaseId))
                .andExpect(status().isBadRequest())
                .andExpect(result ->{
                    JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
                    String errorCode = jsonNode.get("errorCode").asText();
                    String status = jsonNode.get("status").asText();
                    String message = jsonNode.get("message").asText();
                    Assertions.assertEquals("INVALID_CASE_ID",errorCode);
                    Assertions.assertEquals("400",status);
                    Assertions.assertTrue(message.contains("Invalid caseId: must be a positive integer.Current caseId:"+negativeCaseId));
                });
    }

    @Test
    public void shouldThrowIllegalArgumentException(){
        when(caseActionHandlerService.invokeActionHandlerStrRequest(anyString(),anyString(),anyString(),anyString()))
                .thenThrow(new IllegalArgumentException("Illegal Argument"));
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            caseController.createCaseNew2("anyWorkflow","anyAction",requestStr);
        });
    }
    @Test
    public void createCaseWithInvalidWorkflowShouldThrowException() throws Exception {
        //"/v4/{workflow}/action/{action}"
        doThrow(new IllegalArgumentException("Workflow is invalid."))
        .when(caseValidationService).validateWorkflow(anyString());

        MvcResult mvcResult = mockMvc.perform(post("/api/cases/v4/workflow/action/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestStr))
                .andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),mvcResult.getResponse().getStatus());
        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("Workflow is invalid."));
    }

    @Test
    public void createCaseWithInvalidActionShouldThrowException() throws Exception {
        //arrange
        doThrow(new IllegalArgumentException("Action is invalid."))
                .when(caseValidationService).validateAction(anyString());
        //perform request
        MvcResult mvcResult = mockMvc.perform(post("/api/cases/v4/workflow/action/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestStr))
                .andReturn();
        System.out.println("response string printing:"+mvcResult.getResponse().toString());
        //assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),mvcResult.getResponse().getStatus());
        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("Action is invalid."));

    }
}
