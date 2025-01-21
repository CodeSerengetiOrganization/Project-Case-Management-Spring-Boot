package com.mytech.casemanagement.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RequestValidatorTests {
    private final RequestValidator requestValidator = new RequestValidator();

    @Test
    public void testValidateRequestPayloadHappyPathShouldNotThrowException() {
        String validPayload = "{\"key\":\"value\"}";
        Assertions.assertDoesNotThrow(
                () -> requestValidator.validateRequestPayload(validPayload)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    public void testValidateRequestPayloadWithBlankPayloadShouldThrowException(String payload) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            requestValidator.validateRequestPayload(payload);
        });
    }

    @Test
    public void testValidateRequestPayloadWithNullShouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            requestValidator.validateRequestPayload(null);
        });
    }
}
