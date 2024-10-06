package com.mytech.casemanagement.exception.handler;

import com.mytech.casemanagement.exception.CaseResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CaseNewNotProvidedException.class)
    public ResponseEntity<String> handleCaseNewNotProvidedException(CaseNewNotProvidedException e){
        return ResponseEntity.badRequest().body(e.getMessage());    //return http status 400
    }

    @ExceptionHandler(CaseResourceNotFoundException.class)
    public ResponseEntity<String> handleCaseResourceNotFoundException(CaseResourceNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());    //return http status 400
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());    //return http status 400
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());    //return http status 500
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());    //return http status 500
    }

}
