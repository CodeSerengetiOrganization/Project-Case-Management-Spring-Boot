package com.mytech.casemanagement.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CaseValidationServiceTests {
    private final CaseValidationService caseValidationService = new CaseValidationService();

    @Test
    public void testValidateWorkflowHappyPathShouldNotThrowException(){
        String validWorkflow = "workflowA";
        Assertions.assertDoesNotThrow(
                ()->{caseValidationService.validateWorkflow(validWorkflow);}
        );
    }
    @ParameterizedTest
    @ValueSource(strings={"invalidWorkflow",""," "})
    public void testValidateWorkflowWithInvalidWorkflowShouldThrowException(String workflow){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            caseValidationService.validateWorkflow(workflow);
        });
    }

    @Test
    public void testValidateWorkflowWithNullShouldThrowException(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            caseValidationService.validateWorkflow(null);
        });
    }

    @Test
    public void testValidateActionHappyPathShouldNotThrowException() {
        String validAction = "create";
        Assertions.assertDoesNotThrow(
                () -> caseValidationService.validateAction(validAction)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"delete", "", " "})
    public void testValidateActionWithInvalidActionShouldThrowException(String action) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            caseValidationService.validateAction(action);
        });
    }

    @Test
    public void testValidateActionWithNullShouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            caseValidationService.validateAction(null);
        });
    }

}
