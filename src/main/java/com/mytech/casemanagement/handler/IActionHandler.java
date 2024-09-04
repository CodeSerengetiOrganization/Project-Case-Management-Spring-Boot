package com.mytech.casemanagement.handler;

import org.springframework.http.ResponseEntity;

public interface IActionHandler {
    ResponseEntity<?> doAction();
}
