package com.mytech.casemanagement.exception.handler;

import com.mytech.casemanagement.exception.CaseResourceNotFoundException;
import com.mytech.casemanagement.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        handler = new GlobalExceptionHandler();
    }


    @Test
    @DisplayName("Should return bad request status when IllegalArgumentException is thrown")
    public void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<String> response = handler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }

    @Test
    @DisplayName("Should return bad request status when CaseResourceNotFoundException is thrown")
    public void shouldReturnBadRequestWhenCaseResourceNotFoundExceptionIsThrown() {
        CaseResourceNotFoundException exception = new CaseResourceNotFoundException("Resource not found");

        ResponseEntity<?> response = handler.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Resource not found", response.getBody());
    }

    @Test
    @DisplayName("Should return exception message when CaseResourceNotFoundException is thrown")
    public void shouldReturnBadRequestAndExceptionMessageWhenCaseNewNotProvidedExceptionIsThrown() {
        String expectedMessage = "CaseNewNotProvidedException occurred";
        CaseNewNotProvidedException exception = new CaseNewNotProvidedException(expectedMessage);

        ResponseEntity<?> response = handler.handleCaseNewNotProvidedException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
    }

    @Test
    @DisplayName("Should return bad request status when RuntimeException is thrown")
    public void shouldReturnBadRequestWhenRuntimeExceptionIsThrown() {
        RuntimeException exception = new RuntimeException("Runtime exception occurred");

        ResponseEntity<String> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Runtime exception occurred", response.getBody());
    }

    @Test
    @DisplayName("Should return bad request status when Exception is thrown")
    public void shouldReturnBadRequestWhenExceptionIsThrown() {
        Exception exception = new Exception("General exception occurred");

        ResponseEntity<String> response = handler.handleException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("General exception occurred", response.getBody());
    }
}
