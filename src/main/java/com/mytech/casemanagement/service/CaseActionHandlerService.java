package com.mytech.casemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.RequestObject;
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
//        return null;
        String mockedWorkflow=workflow;
        mockedWorkflow="workflowAlhpa";
        CaseActionHandler caseActionHandler=lookupCaseActionHandler(mockedWorkflow,action);

       RequestObject requestObject = getRequestObject(requestStr,RequestObject.class);
        ResponseEntity<?> responseEntity = Optional.ofNullable(requestObject)
                .map(RequestObject::getPayload)
                .map(payload -> {
                    try {
                        CaseNew caseFromPayload = objectMapper.treeToValue(payload, CaseNew.class);
                        System.out.println("caseFromPayload:" + caseFromPayload);
                        return caseActionHandler.doAction();
                    } catch (JsonProcessingException e) {
                        //lambda can not throw checked exceptions, and for now the exception system is not yet created, so temperately use RuntimeException to wrapper it.
                        throw new RuntimeException("Error parsing payload to CaseNew object", e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("requestObject is null or empty or format is not correct;"));
        return responseEntity;
    }

    private RequestObject getRequestObject(String requestStr, Class<RequestObject> requestObjectClass) {
        RequestObject requestObject = null;
        try {
            requestObject = objectMapper.readValue(requestStr, RequestObject.class);
        } catch (JsonProcessingException e) {
            //todo: add logging
            //todo: use customized exception
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
