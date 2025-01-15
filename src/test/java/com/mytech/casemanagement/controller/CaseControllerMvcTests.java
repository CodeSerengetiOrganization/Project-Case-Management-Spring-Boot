package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.handler.CaseActionHandler;
import com.mytech.casemanagement.service.CaseActionHandlerService;
import com.mytech.casemanagement.service.CaseService;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CaseController.class)
public class CaseControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CaseServiceNew caseServiceNew; // Mock the dependent service
    @MockBean
    private CaseService caseService;
    @MockBean
    private CaseActionHandlerService caseActionHandlerService;

    @Test
    public void testGetCaseWithInvalidCaseIdShouldHandledByControllerAdvice() throws Exception {
        // Invalid caseId to trigger the exception
        String invalidCaseId = "abc123";

        // Perform the GET request and include assertions
        mockMvc.perform(get("/api/cases/v3/" + invalidCaseId))
                .andExpect(status().isBadRequest()) // Verify the status is 400 Bad Request
                .andExpect(result -> {
                    String responseMessage = result.getResponse().getContentAsString();
                    String expectedMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s.",
                            invalidCaseId, "caseId", "int");
                    assert responseMessage.equals(expectedMessage) : "Unexpected error message: " + responseMessage;
                });
    }
}
