package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.CaseString;
import com.mytech.casemanagement.service.CaseService;
import com.mytech.casemanagement.service.CaseServiceNew;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;
    @Autowired
    private CaseServiceNew caseServiceNew;

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
