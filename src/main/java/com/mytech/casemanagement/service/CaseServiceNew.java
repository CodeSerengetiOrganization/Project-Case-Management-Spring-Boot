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
        if(caseId <= 0){
            throw new IllegalArgumentException("Case ID must be greater than zero. Requested caseId:"+caseId);
        }
        return caseRepository.findByCaseId(caseId);
//        return caseRepository.findByCaseIdQuery(caseId);
//        return caseRepository.findByCaseIdNativeQuery(caseId);
    }

    public CaseNew saveCase(CaseNew c){
        return caseRepository.save(c);
    }

    public CaseNew updateCase(CaseNew caseNew){

        if(caseNew == null){
            throw new IllegalArgumentException("case instance is missing in when updating");
        }
        int caseIdInPayload=caseNew.getCaseId();
        if(caseIdInPayload == 0){   //todo: need unit test code to double check
            throw new CaseResourceNotFoundException(String.format("caseID not existing in request body"));
        }
        if(caseIdInPayload <0){
            throw new IllegalArgumentException(String.format("caseId [%d] format is less than 0",caseIdInPayload));
        }
        Optional<CaseNew> retrievedCase = caseRepository.findByCaseId(caseIdInPayload);
        if(retrievedCase.isPresent()){
            return caseRepository.save(caseNew);
        }else{
            throw new CaseResourceNotFoundException(String. format("caseID [%d] not existing in database",caseIdInPayload));
        }
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