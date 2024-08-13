package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.repository.CaseRepository;
import com.mytech.casemanagement.repository.CaseRepositoryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CaseServiceNew {

    @Autowired
    private CaseRepositoryNew caseRepository;

/*
    public Optional<Case> getCaseByCaseId(int caseId) {
        return caseRepository.findByCaseId(caseId);
    }
*/

    public Optional<CaseNew> getCaseByCaseId(int caseId) {
        return caseRepository.findByCaseId(caseId);
//        return caseRepository.findByCaseIdQuery(caseId);
//        return caseRepository.findByCaseIdNativeQuery(caseId);
    }

    public CaseNew saveCase(CaseNew c){
        return caseRepository.save(c);
    }

    public CaseNew updateCase(CaseNew caseNew){
/*        //todo: need to crate customized exception for it
        if (null == caseNew) throw new RuntimeException("the case to update is null");*/

        Optional<Integer> caseId = Optional
                .ofNullable(caseNew)
                .map(CaseNew::getCaseId);
        if (caseId.isPresent() && caseId.get()>0){  //use caseId.get()>0 to make sure casId exist in payload, as when caseId is missing in payload, the CaseNew entity will initialized caseId with 0;
            Optional<CaseNew> retrievedCase = caseRepository.findByCaseId(caseId.get());
            if (retrievedCase.isPresent()){
                CaseNew updatedCaseNew = caseRepository.save(caseNew);
                return updatedCaseNew;
            }else{
                throw new RuntimeException("caseID not existing in database");
            }

        }
        throw new RuntimeException("caseID not existing in request body");

 /*       else{

        }
        return updatedCaseNew;*/
    }
}