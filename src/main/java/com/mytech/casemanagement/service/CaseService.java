package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseString;
import com.mytech.casemanagement.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

/*
    public Optional<Case> getCaseByCaseId(int caseId) {
        return caseRepository.findByCaseId(caseId);
    }
*/

    public Optional<Case> getCaseByCaseId(int caseId) {
        return caseRepository.findByCaseId(caseId);
//        return caseRepository.findByCaseIdQuery(caseId);
//        return caseRepository.findByCaseIdNativeQuery(caseId);
    }
}