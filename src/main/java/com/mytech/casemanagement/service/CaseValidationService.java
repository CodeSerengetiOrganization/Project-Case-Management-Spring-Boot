package com.mytech.casemanagement.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CaseValidationService {
    //can also use
    private static final Set<String> VALID_ACTIONS = Set.of("create","update","amend");
    private static final Set<String>  VALID_WORKFLOW = Set.of("workflowA");
    /*
    * To validate if the workflow is valid.
    * For new it is mocked business loGic
    * */
    public void validateWorkflow(String workflow){
        if(workflow == null || !VALID_WORKFLOW.contains(workflow)){
            throw new IllegalArgumentException("Workflow is invalid. current workflow: "+workflow);
        }
    }

    public void validateAction(String action) {

        if(action == null || !VALID_ACTIONS.contains(action) ){
            throw new IllegalArgumentException("Action is invalid. current action: "+action);
        }
    }

}
