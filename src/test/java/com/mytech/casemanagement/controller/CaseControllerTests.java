package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.service.CaseActionHandlerService;
import com.mytech.casemanagement.service.CaseService;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CaseController.class)
public class CaseControllerTests {

    @InjectMocks
    private CaseController caseController;

    @MockBean
    private CaseActionHandlerService caseActionHandlerService;

    @MockBean
    private CaseServiceNew caseServiceNew;
    @MockBean
    private CaseService caseService;    //we do not use it in current version,but have to keep it here to make code run.

    @Autowired
    MockMvc mockMvc;

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
/*        String requestStr="{\n" +
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
                "}";*/
        ResponseEntity<?> mockSuccessfulResponseEntity=ResponseEntity.ok().body("Successful Response");
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");
        when(caseActionHandlerService.invokeActionHandlerStrRequest(anyString(), anyString(), anyString(), anyString()))
                .thenReturn((ResponseEntity)mockSuccessfulResponseEntity);
        ResponseEntity<?> response = caseController.createCaseNew2("dar", "create", requestStr);
        Assertions.assertTrue(response.getStatusCode().value() == 200);
        String body = (String)response.getBody();
        System.out.println("body: "+body);
        Assertions.assertTrue(body.contains("Successful Response"));

/*        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");
        when(caseActionHandlerService.invokeActionHandlerStrRequest(anyString(), anyString(), anyString(), anyString()))
                .thenReturn((ResponseEntity<?>) mockResponse);*/
    }
    @Test
    public void testGetCaseWithCaseIdNotExistingShouldReturnResponseEntityWithErrorMessage(){
        int nonExistingCaseId=98761234;
        when(caseServiceNew.getCaseByCaseId(nonExistingCaseId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = caseController.getCaseByCaseIdNew2(nonExistingCaseId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("Case not found for ID: "+nonExistingCaseId));
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
                    String responseMessage = result.getResponse().getContentAsString();
                    String expectedMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s.",
                            invalidCaseId, "caseId", "int");
                    assert responseMessage.equals(expectedMessage) : "Unexpected error message: " + responseMessage;
                });
    }
    /*
    * A scenario with input caseId is a negative value
    * */
    @Test
    public void testGetCaseWithNegativeCaseIdShouldReturnResponseEntityWithErrorMessage(){
        int negativeCaseId=-1;
        ResponseEntity<?> response = caseController.getCaseByCaseIdNew2(negativeCaseId);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("Invalid caseId: must be a positive integer.Current caseId:"+negativeCaseId));
//        System.out.println(response.getBody().toString());
    }

    @Test
    public void shouldThrowIllegalArgumentException(){
        when(caseActionHandlerService.invokeActionHandlerStrRequest(anyString(),anyString(),anyString(),anyString()))
                .thenThrow(new IllegalArgumentException("Illegal Argument"));
        ResponseEntity<?> response = caseController.createCaseNew2("dar", "create", requestStr);
        Assertions.assertNull(response);
    }
}
