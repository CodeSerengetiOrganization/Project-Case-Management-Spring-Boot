package com.mytech.casemanagement.exception.handler;

import com.mytech.casemanagement.entity.ErrorResponse;
import com.mytech.casemanagement.exception.CaseNewNotProvidedException;
import com.mytech.casemanagement.exception.CaseResourceNotFoundException;
import com.mytech.casemanagement.exception.InvalidCaseIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        ResponseEntity<ErrorResponse> response = handler.handleCaseResourceNotFoundException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = response.getBody();
        assertEquals("Resource not found",body.getMessage());
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
    @DisplayName("Should return exception message when InvalidCaseIdException is thrown")
    public void shouldReturnBadRequestAndExceptionMessageWhenInvalidCaseIdExceptionIsThrown() {
        String expectedMessage = "InvalidCaseIdException occurred";
        InvalidCaseIdException exception = new InvalidCaseIdException(expectedMessage);

        ResponseEntity<ErrorResponse> response = handler.handleInvalidCaseIdException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = (ErrorResponse)response.getBody();
        assertEquals("InvalidCaseIdException occurred",body.getMessage());
    }
    @Test
    @DisplayName("Should return exception message when MethodArgumentTypeMismatchException is thrown")
    public void shouldReturnBadRequestAndExceptionMessageWhenMethodArgumentTypeMismatchExceptionIsThrown() {
/*        String expectedMessage = "MethodArgumentTypeMismatchException occurred";
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(expectedMessage);

        ResponseEntity<ErrorResponse> response = handler.handleInvalidCaseIdException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse body = (ErrorResponse)response.getBody();
        assertEquals("InvalidCaseIdException occurred",body.getMessage());*/
        // Arrange
        String parameterName = "id";
        String invalidValue = "abc";
        String expectedType = "Long";
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);

        when(exception.getName()).thenReturn(parameterName);
        when(exception.getValue()).thenReturn(invalidValue);
        when(exception.getRequiredType()).thenAnswer(invocation -> Long.class); // ✅ Properly handles generic return

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentTypeMismatchException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(String.format("Invalid value type of '%s' for parameter '%s'. Expected type: %s.",
                invalidValue, parameterName, expectedType), response.getBody().getMessage());
        assertEquals(LocalDate.now(), response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("Should return bad request status when RuntimeException is thrown")
    public void shouldReturnBadRequestWhenRuntimeExceptionIsThrown() {
        RuntimeException exception = new RuntimeException("Runtime exception occurred");

        ResponseEntity<String> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Runtime exception occurred", response.getBody());
    }

    @Test
    @DisplayName("Should return bad request status when Exception is thrown")
    public void shouldReturnBadRequestWhenExceptionIsThrown() {
        Exception exception = new Exception("General exception occurred");

        ResponseEntity<String> response = handler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("General exception occurred", response.getBody());
    }
}
