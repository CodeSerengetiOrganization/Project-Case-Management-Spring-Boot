package com.mytech.casemanagement.service;

import com.mytech.casemanagement.entity.CaseNew;
import com.mytech.casemanagement.entity.CaseStatusEnum;
import com.mytech.casemanagement.entity.CaseTypeEnum;
import com.mytech.casemanagement.exception.CaseResourceNotFoundException;
import com.mytech.casemanagement.repository.CaseRepositoryNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CaseServiceNew {

/*    @Autowired
    private CaseRepositoryNew caseRepository;*/
    private CaseRepositoryNew caseRepository;

    @Autowired
    public CaseServiceNew(CaseRepositoryNew repository){
        this.caseRepository = repository;
    }



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
        if (caseId.isPresent()){  //use caseId.get()>0 to make sure casId exist in payload, as when caseId is missing in payload, the CaseNew entity will initialized caseId with 0;
            if (caseId.get() ==0){  //caseId is primitive type int,so it will be 0 if try to get()
                throw new CaseResourceNotFoundException(String.format("caseID not existing in request body"));
            }
            if(caseId.get()<0){
                throw new IllegalArgumentException(String.format("caseId [%d] format is less than 0",caseId.get()));
            }
            Optional<CaseNew> retrievedCase = caseRepository.findByCaseId(caseId.get());
            if (retrievedCase.isPresent()){
                CaseNew updatedCaseNew = caseRepository.save(caseNew);
                return updatedCaseNew;
            }else{
                throw new CaseResourceNotFoundException(String. format("caseID [%d] not existing in database",caseId.get()));
            }

        }
        throw new CaseResourceNotFoundException(String.format("caseID not existing in request body"));

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
                    caseNewEntity.setCaseType(CaseTypeEnum.LOD);
                    break;
                case FRAUD:
                    caseNewEntity.setCaseType(CaseTypeEnum.Fraud);
                    break;
                case NETNEW:
                    caseNewEntity.setCaseType(CaseTypeEnum.NetNew);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown CaseType when converting from Kafka message to entity: "+caseType.toString());
            }

            io.swagger.client.model.CaseNew.CaseStatusEnum caseStatus = caseInMessage.getCaseStatus();
            switch (caseStatus){
                case PENDINGDOCUMENT:
                    caseNewEntity.setCaseStatus(CaseStatusEnum.PendingDocument);
                    break;
                case PENDINGREVIEW:
                    caseNewEntity.setCaseStatus(CaseStatusEnum.PendingReview);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown CaseStatus when converting from Kafka message to entity: "+caseStatus.toString());
            }

        }
        return caseNewEntity;
    }
}