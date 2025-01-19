package com.mytech.casemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.RequestObject;
import com.mytech.casemanagement.exception.CaseParsingException;
import com.mytech.casemanagement.handler.CaseActionHandler;
import com.mytech.casemanagement.handler.CreateCaseActionHandler;
import com.mytech.casemanagement.handler.DefaultCaseActionHandler;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CaseActionHandlerService {

    @Autowired
    CreateCaseActionHandler createCaseActionHandler;

    @Autowired
    DefaultCaseActionHandler defaultCaseActionHandler;

    @Autowired
    ObjectMapper objectMapper;

    public ResponseEntity<?> invokeActionHandler(String methodType, String workflow, String action, CaseNew caseNew) {
//        return null;
        String mockedWorkflow=workflow;
        mockedWorkflow="workflowAlhpa";
        CaseActionHandler caseActionHandler=lookupCaseActionHandler(mockedWorkflow,action);

        return caseActionHandler.doAction();
/*        return ResponseEntity.status(HttpStatus.OK)
                .body("New response body from CaseActionHandlerService");*/
    }

    /*
    * This version is for V4 endpoint to handle String type RequestStr from payload
    * */
public ResponseEntity<?> invokeActionHandlerStrRequest(String methodType, String workflow, String action, String requestStr) {
        // Check if requestStr is null or blank
        if(requestStr == null || requestStr.isBlank()){
            throw new IllegalArgumentException("Payload should not be blank when action is "+action);
        }

        // Mock workflow for now
        String mockedWorkflow="create";
        CaseActionHandler caseActionHandler=lookupCaseActionHandler(mockedWorkflow,action);

        // Parse request object
        RequestObject requestObject = getRequestObject(requestStr,RequestObject.class);
        if(requestObject == null){
            //todo: add logging
            throw new CaseParsingException("Error parsing payload to RequestObject. Please check the request string.");
        }

        try {
            // Convert payload to CaseNew and Perform the action
            CaseNew caseFromPayload = objectMapper.treeToValue(requestObject.getPayload(), CaseNew.class);
            if (requestObject.getPayload() == null || requestObject.getPayload().isEmpty()) {
                throw new CaseParsingException("Payload cannot be empty.");
            }
            return caseActionHandler.doAction();
        }catch(JsonProcessingException e){
            // todo: Log the error for debugging
            // Throw a custom exception with meaningful context
            String message =  String.format("Error parsing request string to [%s]. workflow: [%s], cation:[%s]",CaseNew.class.getSimpleName(),workflow,action);
            throw new CaseParsingException(message);
        }
    }

    private RequestObject getRequestObject(String requestStr, Class<RequestObject> requestObjectClass) {
//        objectMapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            return objectMapper.readValue(requestStr, RequestObject.class);
        } catch (JsonProcessingException e) {
            //todo: add logging
            throw new CaseParsingException("Error parsing payload to RequestObject.");
        }
    }

    private CaseActionHandler lookupCaseActionHandler(String workflow, String action) {
        //todo: need to add workflow related logic
        switch (action){
            case "create":
              return createCaseActionHandler;
            default:
                return defaultCaseActionHandler;
        }
    }
}
