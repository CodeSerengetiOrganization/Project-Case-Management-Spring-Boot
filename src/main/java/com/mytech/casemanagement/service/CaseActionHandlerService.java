package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.CaseNew;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CaseActionHandlerService {
    public ResponseEntity<?> invokeActionHandler(String methodType, String workflow, String action, CaseNew caseNew) {
//        return null;
        return ResponseEntity.status(HttpStatus.OK)
                .body("New response body from CaseActionHandlerService");
    }
}
