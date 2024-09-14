package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.handler.CaseActionHandler;
import com.mytech.casemanagement.handler.CreateCaseActionHandler;
import com.mytech.casemanagement.handler.DefaultCaseActionHandler;
import com.mytech.casemanagement.handler.IActionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CaseActionHandlerService {

    @Autowired
    CreateCaseActionHandler createCaseActionHandler;

    @Autowired
    DefaultCaseActionHandler defaultCaseActionHandler;
    public ResponseEntity<?> invokeActionHandler(String methodType, String workflow, String action, CaseNew caseNew) {
//        return null;
        String mockedWorkflow=workflow;
        mockedWorkflow="workflowAlhpa";
        CaseActionHandler caseActionHandler=lookupCaseActionHandler(mockedWorkflow,action);

        return caseActionHandler.doAction();
/*        return ResponseEntity.status(HttpStatus.OK)
                .body("New response body from CaseActionHandlerService");*/
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
