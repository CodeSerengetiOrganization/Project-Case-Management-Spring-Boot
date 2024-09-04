package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.CaseStatusEnum;
import com.mytech.casemanagement.entity.CaseTypeEnum;
import com.mytech.casemanagement.service.CaseActionHandlerService;
import com.mytech.casemanagement.service.CaseService;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.client.model.RetrieveCaseEmptyRs;

import java.util.Optional;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;
    @Autowired
    private CaseServiceNew caseServiceNew;

    @Autowired
    private CaseActionHandlerService caseActionHandlerService;

    @GetMapping("/{caseId}")
    public ResponseEntity<Case> getCaseByCaseId(@PathVariable int caseId) {
        return caseService.getCaseByCaseId(caseId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("v2/{caseId}")
    public ResponseEntity<CaseNew> getCaseByCaseIdNew(@PathVariable int caseId) {
        return caseServiceNew.getCaseByCaseId(caseId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("v3/{caseId}")
    public ResponseEntity<?> getCaseByCaseIdNew2(@PathVariable int caseId) {
        RetrieveCaseEmptyRs retrieveCaseEmptyRs=new RetrieveCaseEmptyRs();
        try{
            Optional<CaseNew> caseByCaseId = caseServiceNew.getCaseByCaseId(caseId);
            CaseNew caseNewEntity = caseByCaseId.get();
            if (null !=caseNewEntity){
//                io.swagger.client.model.CaseNew convertedCaseNew=(io.swagger.client.model.CaseNew) caseNewEntity;
//                io.swagger.client.model.CaseNew swaggerCaseNew=CaseNewMapper.INSTANCE.toSwaggerCaseNew(caseNewEntity);
                io.swagger.client.model.CaseNew convertedSwaggerCaseNew=converToSwaggerCaseNew(caseNewEntity);
                retrieveCaseEmptyRs.setCase(convertedSwaggerCaseNew);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(retrieveCaseEmptyRs, HttpStatus.OK);
    }

    private io.swagger.client.model.CaseNew converToSwaggerCaseNew(CaseNew caseNewEntity) {
        io.swagger.client.model.CaseNew returnCase=new io.swagger.client.model.CaseNew();
        if (caseNewEntity !=null){
            returnCase.setCaseId(caseNewEntity.getCaseId());
            returnCase.setCreatedBy(caseNewEntity.getCreatedBy());
            returnCase.setCreateDate(caseNewEntity.getCreateDate());
            returnCase.setModifiedDate(caseNewEntity.getModifiedDate());
            returnCase.setPendingReviewDate(caseNewEntity.getPendingReviewDate());
            returnCase.setNote(caseNewEntity.getNote());
            CaseStatusEnum caseStatus = caseNewEntity.getCaseStatusEnum();
            switch (caseStatus) {
                case PendingDocument:
                    returnCase.setCaseStatus(io.swagger.client.model.CaseNew.CaseStatusEnum.PENDINGDOCUMENT);
                    break;
                case PendingReview:
                    returnCase.setCaseStatus(io.swagger.client.model.CaseNew.CaseStatusEnum.PENDINGREVIEW);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown CaseStatusEnum value: " + caseStatus);
            }
            CaseTypeEnum caseType = caseNewEntity.getCaseTypeEnum();
            switch (caseType) {
                case Fraud:
                    returnCase.setCaseType(io.swagger.client.model.CaseNew.CaseTypeEnum.FRAUD);
                    break;
                case NetNew:
                    returnCase.setCaseType(io.swagger.client.model.CaseNew.CaseTypeEnum.NETNEW);
                    break;
                case LOD:
                    returnCase.setCaseType(io.swagger.client.model.CaseNew.CaseTypeEnum.LOD);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown CaseTypeEnum value: " + caseStatus);
            }

        }
        return returnCase;
    }

    /*    @GetMapping("/{caseId}")
        public ResponseEntity<CaseString> getCaseByCaseId(@PathVariable int caseId) {
            ResponseEntity<CaseString> responseEntity = caseService.getCaseByCaseId(caseId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
            System.out.println(responseEntity);
            return responseEntity;
        }*/
/*
* Create a new case in db
* */
    @PostMapping    //should use URL:http://localhost:8080/api/cases
//    @PostMapping("/") //shou use URL: http://localhost:8080/api/cases/
    public ResponseEntity<Case> createCase(@RequestBody Case c){
        Case savedCase = caseService.saveCase(c);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCase);

    }
    @PostMapping("/v2")
    public ResponseEntity<?> createCaseNew(@RequestBody CaseNew caseNew){

            CaseNew createdCase = caseServiceNew.saveCase(caseNew);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCase);
    }
/*
* This is the V3 version, covered by ticket12
* in this version, will use invokeHandler() method according to Strategy design pattern
* */
    @PostMapping("/v3/{workflow}/action/{action}")
    public ResponseEntity<?> createCaseNew2(
            @PathVariable("workflow") String workflow,
            @PathVariable("action") String action,
            @RequestBody CaseNew caseNew){
        return invokeActionHandler(HttpMethod.POST.toString(), workflow,action,caseNew);
    }

    private ResponseEntity<?> invokeActionHandler(String methodType, String workflow, String action, CaseNew caseNew) {
        return caseActionHandlerService.invokeActionHandler(methodType, workflow, action, caseNew);
    }

    @PatchMapping
    public ResponseEntity<Case> updateCase(@RequestBody Case c){
            Case updatedCase = caseService.updateCase(c);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCase);
    }

    @PatchMapping("/v2")
    public ResponseEntity<?> updateCaseNew(@RequestBody CaseNew caseNew){
        try {
        CaseNew updatedCase = caseServiceNew.updateCase(caseNew);
        return ResponseEntity.ok(updatedCase);
        }catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred when updating case: "+runtimeException.getMessage());
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred when updating case: "+exception.getMessage());
        }
    }
}
