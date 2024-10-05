package com.mytech.casemanagement.exception;

public class CaseResourceNotFoundException extends RuntimeException{
    public CaseResourceNotFoundException(String message) {
        super(message);
    }
}
