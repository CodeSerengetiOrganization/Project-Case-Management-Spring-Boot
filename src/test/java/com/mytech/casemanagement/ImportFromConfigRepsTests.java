package com.mytech.casemanagement;

import io.swagger.client.model.CaseNew;
import org.junit.Test;


public class ImportFromConfigRepsTests {
    @Test
    public void printCaseNewFromConfigRepo(){
        CaseNew casenew = new CaseNew();
        casenew.setCaseId(12345);
        System.out.println("This is a CaeNew object created from config repo:"+casenew);
    }

}
