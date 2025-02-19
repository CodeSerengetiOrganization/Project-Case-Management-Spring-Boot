package com.mytech.casemanagement.handler;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.exception.CaseNullException;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCaseActionHandlerTest {

    @InjectMocks
    private CreateCaseActionHandler createCaseActionHandler;

    @Mock
    private CaseServiceNew caseServiceNew;

    @Mock
    private CaseNew caseNew;

    @Test
    public void testDoAction_caseNewIsNull() {
        // Act & Assert

        CaseNullException caseNullException = Assertions.assertThrows(CaseNullException.class, () -> {
            createCaseActionHandler.doAction(null);
        });
        Assertions.assertTrue(caseNullException.getMessage().contains("The CaseNew instance to save into the database should not be null."));
    }

    @Test
    public void testDoAction_caseNewIsValid() {
        // Arrange
        CaseNew caseNew = new CaseNew(); // Assuming you have a valid constructor or setter methods
        CaseNew savedCase = new CaseNew(); // This would be the saved case object
        when(caseServiceNew.saveCase(caseNew)).thenReturn(savedCase);

        // Act
        ResponseEntity<?> response = createCaseActionHandler.doAction(caseNew);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(savedCase, response.getBody());
    }
}
