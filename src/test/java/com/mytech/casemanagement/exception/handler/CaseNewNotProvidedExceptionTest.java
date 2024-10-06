package com.mytech.casemanagement.exception.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CaseNewNotProvidedExceptionTest {

    @Test
    @DisplayName("Should throw CaseNewNotProvidedException when message is provided")
    public void shouldThrowCaseNewNotProvidedExceptionWhenMessageIsProvided() {
        assertThrows(CaseNewNotProvidedException.class, () -> {
            throw new CaseNewNotProvidedException("Test message");
        });
    }

    @Test
    @DisplayName("Should contain correct message when CaseNewNotProvidedException is thrown")
    public void shouldContainCorrectMessageWhenCaseNewNotProvidedExceptionIsThrown() {
        Exception exception = assertThrows(CaseNewNotProvidedException.class, () -> {
            throw new CaseNewNotProvidedException("Test message");
        });

        assertEquals("Test message", exception.getMessage());
    }
}
