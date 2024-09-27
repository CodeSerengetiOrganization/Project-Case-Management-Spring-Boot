package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.CaseStatusEnum;
import com.mytech.casemanagement.entity.CaseTypeEnum;
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
/*
* to covert a CaseNew from Kafka message to a CaseNew Entity
* Note: In Kafka message ,the CaseNew is constrained by schema, which means constrained by the swagger generated java class,
* but for case management system, it has to use CaseNew Entity, which is created manually by developer as swagger can NOT add @Table annotation automaticlally.
* */
    public CaseNew mapToCaseNewEntity(io.swagger.client.model.CaseNew caseInMessage) {
        CaseNew caseNewEntity=new CaseNew();
        if (caseInMessage != null){
            caseNewEntity.setNote(caseInMessage.getNote());
            caseNewEntity.setCreatedBy(caseInMessage.getCreatedBy());
/*            caseNewEntity.setCreateDate(caseInMessage.getCreateDate());
            caseNewEntity.setModifiedDate(caseInMessage.getModifiedDate());
            caseNewEntity.setPendingReviewDate(caseInMessage.getPendingReviewDate());*/

            io.swagger.client.model.CaseNew.CaseTypeEnum caseType = caseInMessage.getCaseType();
            switch (caseType){
                case LOD:
                    caseNewEntity.setCaseTypeEnum(CaseTypeEnum.LOD);
                    break;
                case FRAUD:
                    caseNewEntity.setCaseTypeEnum(CaseTypeEnum.Fraud);
                    break;
                case NETNEW:
                    caseNewEntity.setCaseTypeEnum(CaseTypeEnum.NetNew);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown CaseType when converting from Kafka message to entity: "+caseType.toString());
            }

            io.swagger.client.model.CaseNew.CaseStatusEnum caseStatus = caseInMessage.getCaseStatus();
            switch (caseStatus){
                case PENDINGDOCUMENT:
                    caseNewEntity.setCaseStatusEnum(CaseStatusEnum.PendingDocument);
                    break;
                case PENDINGREVIEW:
                    caseNewEntity.setCaseStatusEnum(CaseStatusEnum.PendingReview);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown CaseStatus when converting from Kafka message to entity: "+caseStatus.toString());
            }

        }
        return caseNewEntity;
    }
}