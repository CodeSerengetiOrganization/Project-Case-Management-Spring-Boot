package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.service.CaseActionHandlerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaseControllerTests {

    @InjectMocks
    private CaseController caseController;

    @Mock
    private CaseActionHandlerService caseActionHandlerService;

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
    public void shouldThrowIllegalArgumentException(){
        when(caseActionHandlerService.invokeActionHandlerStrRequest(anyString(),anyString(),anyString(),anyString()))
                .thenThrow(new IllegalArgumentException("Illegal Argument"));
        ResponseEntity<?> response = caseController.createCaseNew2("dar", "create", requestStr);
        Assertions.assertNull(response);
    }
}
