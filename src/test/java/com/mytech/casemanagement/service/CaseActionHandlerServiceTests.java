package com.mytech.casemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.RequestObject;
import com.mytech.casemanagement.exception.CaseParsingException;
import com.mytech.casemanagement.handler.CaseActionHandler;
import com.mytech.casemanagement.handler.CreateCaseActionHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CaseActionHandlerServiceTests {

    @InjectMocks
    CaseActionHandlerService caseActionHandlerService;
    @Mock
    CreateCaseActionHandler createCaseActionHandler;
    @Mock
    ObjectMapper objectMapper;

    @Test
    public void invokeActionHandlerStrRequestShouldPass() throws JsonProcessingException {
        String goodRequestStr="{\n" +
                "  \"action\": \"create\",\n" +
                "  \"payload\": {\n" +
                "    \"caseId\": 1,\n" +
                "    \"caseStatus\": \"PendingDocument\",\n" +
                "    \"caseType\": \"NetNew\",\n" +
                "    \"createdBy\": \"Tony Stark\",\n" +
                "    \"createDate\": \"2024-01-01T12:00:00\",\n" +
                "    \"modifiedDate\": \"2024-01-02T12:00:00\",\n" +
                "    \"pendingReviewDate\": \"2024-01-03T12:00:00\",\n" +
                "    \"note\": \"Good payload in Unit test CaseActionHandlerServiceTests.\",\n" +
                "  }\n" +
                "}";

        ResponseEntity<String> mockedResponse = ResponseEntity.status(HttpStatus.OK)
                .body("Mocked Successful Response from unit test");

        CaseNew mockedCase = new CaseNew();
        mockedCase.setCaseId(1234);

        //mock behavior inside CaseActionHandlerService
        when(objectMapper.readValue(any(String.class), eq(RequestObject.class)))
                .thenReturn(new RequestObject());
        when(objectMapper.treeToValue(any(), eq(CaseNew.class)))
                .thenReturn(mockedCase);
        when(createCaseActionHandler.doAction())
                .thenReturn((ResponseEntity) mockedResponse);
/*        BDDMockito.given(createCaseActionHandler.doAction())
            .willReturn((ResponseEntity) mockedResponse);*/
        //invoke method and assert
        ResponseEntity<?> response = caseActionHandlerService.invokeActionHandlerStrRequest("methodTypeNotUsing", "workFlowCanBeMocked", "create", goodRequestStr);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("Mocked Successful Response from unit test"));
    }
    @Test
    public void invokeActionHandlerStrRequestWithNullRequestStrShouldThrowException(){
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->caseActionHandlerService.invokeActionHandlerStrRequest("methodTypeNotUsing", "workFlowCanBeMocked", "create", null),
                "no exception thrown, or unexpected exception type thrown");

    }

    @Test
    public void invokeActionHandlerStrRequestWithBlankRequestStrShouldThrowException(){
        String blankRequestStr = " ";
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->caseActionHandlerService.invokeActionHandlerStrRequest("methodTypeNotUsing", "workFlowCanBeMocked", "create", blankRequestStr),
                "no exception thrown, or unexpected exception type thrown");
    }
    @Test
    public void invokeActionHandlerStrRequestWithIncorrectRequestStrShouldThrowException(){
        String incorrectJsonFormatRequestStr = "***\n" +    //adding *** to destroy json format
                "    \"action\":\"create\",\n" +
                "    \"caseId\": 51,\n" +
                "    \"caseStatus\": \"PendingDocument\",\n" +
                "    \"caseType\": \"NetNew\",\n" +
                "    \"createdBy\": \"Tony Stark\",\n" +
                "    \"createDate\": \"2024-09-07T18:59:35\",\n" +
                "    \"modifiedDate\": \"2024-09-08T18:59:35\",\n" +
                "    \"pendingReviewDate\": \"2024-09-09T18:59:35\",\n" +
                "    \"note\": \"Good payload in Unit test CaseActionHandlerServiceTests.\"\n" +
                "}";
        Assertions.assertThrows(CaseParsingException.class,
                ()->caseActionHandlerService.invokeActionHandlerStrRequest("methodTypeNotUsing", "workFlowCanBeMocked", "create", incorrectJsonFormatRequestStr),
                "no exception thrown, or unexpected exception type thrown");

    }
    @Test
    public void invokeActionHandlerStrRequestWithIncorrectCaseNewFormatShouldThrowException(){
        String requestStrWithExtraField = "{\n" +
                "    \"action\":\"create\",\n" +
                "    \"caseId\": 51,\n" +
                "    \"caseStatus\": \"PendingDocument\",\n" +
                "    \"caseType\": \"NetNew\",\n" +
                "    \"createdBy\": \"Tony Stark\",\n" +
                "    \"createDate\": \"2024-09-07T18:59:35\",\n" +
                "    \"modifiedDate\": \"2024-09-08T18:59:35\",\n" +
                "    \"pendingReviewDate\": \"2024-09-09T18:59:35\",\n" +
                "    \"note\": \"Good payload in Unit test CaseActionHandlerServiceTests.\"\n" +
                "    \"extraField\": \"extraField not existing in CaseNew class.\"\n" + //add extra field that is not defined in CaseNew class
                "}";
        Assertions.assertThrows(CaseParsingException.class,
                ()->caseActionHandlerService.invokeActionHandlerStrRequest("methodTypeNotUsing", "workFlowCanBeMocked", "create", requestStrWithExtraField),
                "no exception thrown, or unexpected exception type thrown");
    }
}
