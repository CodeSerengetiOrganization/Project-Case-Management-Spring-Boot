package com.mytech.casemanagement.validator;

import org.springframework.stereotype.Service;

@Service
public class RequestValidator {
    public void validateRequestPayload(String requestStr){
        if(requestStr == null || requestStr.isBlank()){
            throw new IllegalArgumentException("Payload should not be blank when action is "+requestStr);
        }
    }
}
