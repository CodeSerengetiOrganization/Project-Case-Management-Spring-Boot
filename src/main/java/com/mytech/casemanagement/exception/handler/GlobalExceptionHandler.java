package com.mytech.casemanagement.exception.handler;

import com.mytech.casemanagement.entity.ErrorResponse;
import com.mytech.casemanagement.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String invalidCaseIdExceptionErrorCode = "INVALID_CASE_ID";
    public static final String caseResourceNotFoundExceptionErrorCode = "CASE_RESOURCE_NOT_FOUND";
    public static final int httpBadRequestStatus = 400;
    public GlobalExceptionHandler() {
        System.out.println("GlobalExceptionHandler initialized");
    }

    @ExceptionHandler(CaseNewNotProvidedException.class)
    public ResponseEntity<String> handleCaseNewNotProvidedException(CaseNewNotProvidedException e){
        return ResponseEntity.badRequest().body(e.getMessage());    //return http status 400
    }

    @ExceptionHandler(CaseResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCaseResourceNotFoundException(CaseResourceNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(caseResourceNotFoundExceptionErrorCode);
        errorResponse.setStatus(httpBadRequestStatus);
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDate.now());
        return ResponseEntity.badRequest().body(errorResponse);    //return http status 400
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());    //return http status 400
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        String message = String.format("Invalid value type of '%s' for parameter '%s'. Expected type: %s.",
                e.getValue(), e.getName(), e.getRequiredType().getSimpleName());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(invalidCaseIdExceptionErrorCode);
        errorResponse.setStatus(httpBadRequestStatus);
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(LocalDate.now());
//        System.out.println("message from GlobalExceptionHandler:"+message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(CaseParsingException.class)
    public ResponseEntity<String> handleCaseParsingException(CaseParsingException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CaseNullException.class)
    public ResponseEntity<String> handleCaseNullException(CaseNullException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidCaseIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCaseIdException(InvalidCaseIdException e){
        ErrorResponse errorResponse =new ErrorResponse();
        errorResponse.setErrorCode(invalidCaseIdExceptionErrorCode);
        errorResponse.setStatus(httpBadRequestStatus);
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(LocalDate.now());
        return ResponseEntity.badRequest().body(errorResponse);
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
