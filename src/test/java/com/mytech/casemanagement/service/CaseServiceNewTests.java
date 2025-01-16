package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.CaseStatusEnum;
import com.mytech.casemanagement.entity.CaseTypeEnum;
import com.mytech.casemanagement.exception.CaseResourceNotFoundException;
import com.mytech.casemanagement.repository.CaseRepository;
import com.mytech.casemanagement.repository.CaseRepositoryNew;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest
public class CaseServiceNewTests {

    @InjectMocks
    CaseServiceNew caseServiceNew;

    @Mock
    CaseRepositoryNew caseRepositoryNew;

    private CaseNew caseNew = new CaseNew();
    private CaseNew caseWithNoId = new CaseNew();

    @BeforeEach
    public void setup(){
        caseNew.setCaseId(123456);
        caseNew.setCaseType(CaseTypeEnum.LOD);
        caseNew.setCaseStatus(CaseStatusEnum.PendingDocument);
        caseNew.setNote("created by unit test code");
        caseNew.setCreatedBy("Elon Mush");
        caseNew.setCreateDate(LocalDateTime.now());
        caseNew.setModifiedDate(LocalDateTime.now());
        caseNew.setPendingReviewDate(LocalDateTime.now());

        caseWithNoId.setCaseType(CaseTypeEnum.LOD);
        caseWithNoId.setCaseStatus(CaseStatusEnum.PendingDocument);
        caseWithNoId.setNote("created by unit test code");
        caseWithNoId.setCreatedBy("Elon Mush");
        caseWithNoId.setCreateDate(LocalDateTime.now());
        caseWithNoId.setModifiedDate(LocalDateTime.now());
        caseWithNoId.setPendingReviewDate(LocalDateTime.now());

    }

    @Test
    public void retrieveNegativeCaseIdShouldThrowException(){
        int negativeCaseId=-1;
        Assertions.assertThrows(IllegalArgumentException.class, ()->caseServiceNew.getCaseByCaseId(negativeCaseId));
    }

    @Test
    public void updateCaseShouldThrowExceptionWhenNoCaseIdInParameter(){
        CaseResourceNotFoundException exception = Assertions.assertThrows(CaseResourceNotFoundException.class, () -> {
            caseServiceNew.updateCase(caseWithNoId);
        });
        Assertions.assertTrue(exception.getMessage().contains("not existing in request body"));

    }
    @Test
    public void updateCaseShouldThrowExceptionWhenCaseIdLessThanZeroInParameter(){
        caseNew.setCaseId(-1);
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            caseServiceNew.updateCase(caseNew);
        });
        Assertions.assertTrue(exception.getMessage().contains("format is less than 0"));

    }

    @Test
    public void updateCaseShouldThrowExceptionWhenCaseIdNotExistInDb(){
        caseNew.setCaseId(123456);
        when(caseRepositoryNew.findByCaseId(anyInt()))
                .thenReturn(Optional.ofNullable(null));
        CaseResourceNotFoundException exception = Assertions.assertThrows(CaseResourceNotFoundException.class, () -> {
            caseServiceNew.updateCase(caseNew);
        });
//        System.out.println("exception message: "+exception.getMessage());
        Assertions.assertTrue(exception.getMessage().contains("not existing in database"));

    }
/*
    @Test
    public void printRepositoryObject(){
        System.out.println("case"+caseRepository);
    }*/
}
