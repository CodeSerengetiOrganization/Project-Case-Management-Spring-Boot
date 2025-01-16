package com.mytech.casemanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mytech.casemanagement.entity.*;
import com.mytech.casemanagement.service.CaseActionHandlerService;
import com.mytech.casemanagement.service.CaseService;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.client.model.RetrieveCaseEmptyRs;

import java.time.ZoneOffset;
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
        //1. handle invalid caseId input
        if(caseId<=0){
            return new ResponseEntity<>("Invalid caseId: must be a positive integer.Current caseId:"+caseId,HttpStatus.BAD_REQUEST);
        }
        return caseServiceNew.getCaseByCaseId(caseId)
                .<ResponseEntity<?>> map(caseNew -> {
                    RetrieveCaseEmptyRs retrieveCaseEmptyRs=new RetrieveCaseEmptyRs();
                    io.swagger.client.model.CaseNew convertedSwaggerCaseNew=converToSwaggerCaseNew(caseNew);
                    retrieveCaseEmptyRs.setCase(convertedSwaggerCaseNew);
                    return ResponseEntity.status(HttpStatus.OK).body(retrieveCaseEmptyRs);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Case not found for ID: " + caseId));

    }
    
    private io.swagger.client.model.CaseNew converToSwaggerCaseNew(CaseNew caseNewEntity) {
        io.swagger.client.model.CaseNew returnCase=new io.swagger.client.model.CaseNew();
        if (caseNewEntity !=null){
            returnCase.setCaseId(caseNewEntity.getCaseId());
            returnCase.setCreatedBy(caseNewEntity.getCreatedBy());
            ZoneOffset offset= ZoneOffset.UTC;
            returnCase.setCreateDate(caseNewEntity.getCreateDate().atOffset(offset));
            returnCase.setModifiedDate(caseNewEntity.getModifiedDate().atOffset(offset));
            returnCase.setPendingReviewDate(caseNewEntity.getPendingReviewDate().atOffset(offset));
            returnCase.setNote(caseNewEntity.getNote());
            CaseStatusEnum caseStatus = caseNewEntity.getCaseStatus();
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
            CaseTypeEnum caseType = caseNewEntity.getCaseType();
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

    /*
     * This is the V4 version, covered by ticket29
     * in this version, will use String type to receive request body, this way is more generic for ohter actions in future
     * */
    @PostMapping("/v4/{workflow}/action/{action}")
    public ResponseEntity<?> createCaseNew2(
            @PathVariable("workflow") String workflow,
            @PathVariable("action") String action,
            @RequestBody String requestStr){
        System.out.println("workflow:"+workflow);
        return invokeActionHandler4(HttpMethod.POST.toString(), workflow,action,requestStr);

    }

    private ResponseEntity<?> invokeActionHandler4(String methodType, String workflow, String action, String requestStr) {
        // todo: should try-catch all known exceptions and handle them.
        //todo: should log all the exceptions.
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = caseActionHandlerService.invokeActionHandlerStrRequest(methodType, workflow, action, requestStr);
        } catch (IllegalArgumentException e) {
//            throw new RuntimeException(e);
            System.out.println("catch exception in Controller: "+ e.getMessage());
        }catch (RuntimeException e){
            System.out.println("catch exception in Controller: "+ e.getMessage());
        }catch (Exception e) {
            System.out.println("catch exception in Controller: "+ e.getMessage());
        }
        return responseEntity;
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
