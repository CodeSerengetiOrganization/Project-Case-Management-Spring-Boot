package com.mytech.casemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.RequestObject;
import com.mytech.casemanagement.exception.CaseParsingException;
import com.mytech.casemanagement.handler.CaseActionHandler;
import com.mytech.casemanagement.handler.CaseActionHandlerRegistry;
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

    @Autowired
    CaseActionHandlerRegistry caseActionHandlerRegistry;

    // Add setter for testing purposes
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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
        String mockedWorkflow="workflowA";
        CaseActionHandler caseActionHandler=lookupCaseActionHandler(mockedWorkflow,action);

        // Parse request object
        RequestObject requestObject = getRequestObject(requestStr,RequestObject.class);
        if(requestObject == null){
            //todo: add logging
            throw new CaseParsingException("Error parsing payload to RequestObject. Please check the request string.");
        }

        try {
            // Convert payload to CaseNew and Perform the action
            JsonNode payloadNode = requestObject.getPayload();
            System.out.println("payloadNode:"+payloadNode);
            CaseNew caseFromPayload = objectMapper.treeToValue(payloadNode, CaseNew.class);
//            CaseNew caseFromPayload = objectMapper.treeToValue((JsonNode)requestObject.getPayload(), CaseNew.class);
            if (requestObject.getPayload() == null || requestObject.getPayload().isEmpty()) {
                throw new CaseParsingException("Payload cannot be empty.");
            }
            return caseActionHandler.doAction(caseFromPayload);
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
        return caseActionHandlerRegistry.getHandler(action);
/*        switch (action){
            case "create":
              return createCaseActionHandler;
            default:
                return defaultCaseActionHandler;
        }*/
    }
}
