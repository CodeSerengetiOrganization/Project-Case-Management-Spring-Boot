package com.mytech.casemanagement;

import io.swagger.client.model.CaseNew;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;


public class ImportFromConfigRepsTestsJunit5 {
    @Test
    public void printCaseNewFromConfigRepo(){
        CaseNew casenew = new CaseNew();
        casenew.setCaseId(12345);
        System.out.println("This is a CaeNew object created from config repo:"+casenew);
        Assertions.assertEquals(12345,casenew.getCaseId());
    }

}
