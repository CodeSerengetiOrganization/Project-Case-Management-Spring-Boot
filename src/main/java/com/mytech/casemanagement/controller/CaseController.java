package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseString;
import com.mytech.casemanagement.service.CaseService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @GetMapping("/{caseId}")
    public ResponseEntity<Case> getCaseByCaseId(@PathVariable int caseId) {
        return caseService.getCaseByCaseId(caseId)
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
        return ResponseEntity.ok(savedCase);
    }


}
