package com.mytech.casemanagement.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CreateCaseActionHandler implements CaseActionHandler{
    @Override
    public ResponseEntity<?> doAction() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("New response body from CreateCaseActionHandler, this doAction method ");
    }
}
