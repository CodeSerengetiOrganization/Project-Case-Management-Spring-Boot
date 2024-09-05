package com.mytech.casemanagement.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DefaultCaseActionHandler implements CaseActionHandler{
    @Override
    public ResponseEntity<?> doAction() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("This is from DefaultCaseActionHandler.doAction() method");
    }
}
