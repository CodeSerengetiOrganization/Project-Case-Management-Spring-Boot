package com.mytech.casemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.RequestObject;
import com.mytech.casemanagement.handler.CaseActionHandler;
import com.mytech.casemanagement.handler.CreateCaseActionHandler;
import com.mytech.casemanagement.handler.DefaultCaseActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
//        return null;
        String mockedWorkflow=workflow;
        mockedWorkflow="workflowAlhpa";
        CaseActionHandler caseActionHandler=lookupCaseActionHandler(mockedWorkflow,action);

        RequestObject requestObject = getRequestObject(requestStr,RequestObject.class);
        CaseNew caseFromPayload = null;
        try {
            caseFromPayload = objectMapper.treeToValue(requestObject.getPayload(), CaseNew.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("caseFromPayload:"+caseFromPayload);
        return caseActionHandler.doAction();

    }

    private RequestObject getRequestObject(String requestStr, Class<RequestObject> requestObjectClass) {
        RequestObject requestObject = null;
        try {
            requestObject = objectMapper.readValue(requestStr, RequestObject.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return requestObject;
    }

    private CaseActionHandler lookupCaseActionHandler(String workflow, String action) {
        //tod: need to add workflow related logic
        switch (action){
            case "create":
              return createCaseActionHandler;
            default:
                return defaultCaseActionHandler;
        }
    }
}
