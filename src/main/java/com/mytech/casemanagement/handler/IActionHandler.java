package com.mytech.casemanagement.handler;

import com.mytech.casemanagement.entity.CaseNew;
import org.springframework.http.ResponseEntity;

public interface IActionHandler {
    ResponseEntity<?> doAction(CaseNew caseNew);
}
