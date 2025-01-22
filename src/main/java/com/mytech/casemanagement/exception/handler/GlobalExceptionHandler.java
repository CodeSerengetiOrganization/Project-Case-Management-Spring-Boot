package com.mytech.casemanagement.exception.handler;

import com.mytech.casemanagement.exception.CaseNewNotProvidedException;
import com.mytech.casemanagement.exception.CaseNullException;
import com.mytech.casemanagement.exception.CaseParsingException;
import com.mytech.casemanagement.exception.CaseResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public GlobalExceptionHandler() {
        System.out.println("GlobalExceptionHandler initialized");
    }

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
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s.",
                e.getValue(), e.getName(), e.getRequiredType().getSimpleName());
//        System.out.println("message from GlobalExceptionHandler:"+message);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(CaseParsingException.class)
    public ResponseEntity<String> handleCaseParsingException(CaseParsingException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CaseNullException.class)
    public ResponseEntity<String> handleCaseNullException(CaseNullException e){
        return ResponseEntity.badRequest().body(e.getMessage());
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
