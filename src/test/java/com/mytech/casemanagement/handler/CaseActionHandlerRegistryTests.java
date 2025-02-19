package com.mytech.casemanagement.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseActionHandlerRegistryTests {
//    @InjectMocks
    private CaseActionHandlerRegistry caseActionHandlerRegistry;

    @Mock
    private CreateCaseActionHandler createHandlerMock;

    @Mock
    private DefaultCaseActionHandler defaultCaseActionHandlerMock;

    @BeforeEach
    void setUp() {
        // Define behavior for mocked handlers
        when(createHandlerMock.getActionName()).thenReturn("create");
        when(defaultCaseActionHandlerMock.getActionName()).thenReturn("default");

        // Initialize the registry once for all tests
        List<CaseActionHandler> handlers = Arrays.asList(createHandlerMock, defaultCaseActionHandlerMock);
        caseActionHandlerRegistry = new CaseActionHandlerRegistry(handlers);
    }

    @Test
    void testGetHandler_ValidAction() {

        // Test for "create" action
        CaseActionHandler createHandler = caseActionHandlerRegistry.getHandler("create");
        assertNotNull(createHandler, "Handler for 'create' should not be null");
        assertEquals(createHandlerMock, createHandler, "Handler for 'create' should match the mocked instance");

        // Test for default
        CaseActionHandler defaultHandler = caseActionHandlerRegistry.getHandler("default");
        assertNotNull(defaultHandler, "Handler for 'default' should not be null");
        assertEquals(defaultCaseActionHandlerMock, defaultHandler, "Handler for 'default' should match the mocked instance");


    }

    @Test
    void testGetHandler_InvalidAction() {
        String unsupportedAction = "invalid action";

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> caseActionHandlerRegistry.getHandler(unsupportedAction),
                "Expected exception for unsupported action"
        );

        assertEquals("No handler found for action: " + unsupportedAction, exception.getMessage());
    }

    @Test
    void testHandlersAreRegisteredCorrectly() {

        verify(createHandlerMock, atLeastOnce()).getActionName();
        verify(defaultCaseActionHandlerMock, atLeastOnce()).getActionName();
    }

}
