package com.mytech.casemanagement.handler;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.exception.CaseNullException;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CreateCaseActionHandler implements CaseActionHandler{
    @Autowired
    CaseServiceNew caseServiceNew;
    @Override
    public ResponseEntity<?> doAction(CaseNew caseNew) {
        if(caseNew == null){
            throw new CaseNullException("The CaseNew instance to save into the database should not be null.");
        }
        CaseNew savedCase = caseServiceNew.saveCase(caseNew);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCase);
    }
}
