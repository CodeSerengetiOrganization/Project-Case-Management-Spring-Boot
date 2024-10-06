package com.mytech.casemanagement.exception.handler;

public class CaseNewNotProvidedException extends RuntimeException {
    public CaseNewNotProvidedException(String message) {
        super(message);
    }
}