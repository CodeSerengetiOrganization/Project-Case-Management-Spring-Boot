package com.mytech.casemanagement.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ErrorResponse {
    private String  errorCode;
    private String message;
    private int status;
    private LocalDate timestamp;
}

