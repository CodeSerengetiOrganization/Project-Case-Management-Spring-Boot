package com.mytech.casemanagement.controller;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseString;
import com.mytech.casemanagement.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
