package com.mytech.casemanagement.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.mytech.casemanagement.exception.handler") // Ensure the handler is scanned
public class CaseControllerSpringBootTests {
    @Autowired
    MockMvc mockMvc;

/*    @MockBean
    private CaseService caseService; // Mocking the service dependency*/
    /*
     * A scenario with input caseId is string rather than an integer
     * */
    @Test
    public void testGetCaseWithInvalidCaseIdShouldHandledByControllerAdvice() throws Exception {
        String invalidCaseId = "abc123";
        MvcResult mvcResult = mockMvc.perform(get("/api/cases/v3/"+invalidCaseId))
                .andExpect(status().isBadRequest())
                .andReturn();
        String returnedMessage = mvcResult.getResponse().getContentAsString();
        String explectedMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s.",
                invalidCaseId, "caseId", "int");
        Assertions.assertEquals(explectedMessage,returnedMessage);

    }
}
