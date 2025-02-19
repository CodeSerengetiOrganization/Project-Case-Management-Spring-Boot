package com.mytech.casemanagement.mapper;

import io.swagger.client.model.CaseNew;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CaseNewMapper {

/*    CaseNewMapper INSTANCE = Mappers.getMapper(CaseNewMapper.class);

    io.swagger.client.model.CaseNew toSwaggerCaseNew(com.mytech.casemanagement.entity.CaseNew entityCaseNew);

    com.mytech.casemanagement.entity.CaseNew toEntityCaseNew(io.swagger.client.model.CaseNew swaggerCaseNew);

    // Custom mapping for the enum
    @Mapping(target = "caseStatus", expression = "java(mapCaseStatus(entityCaseNew.getCaseStatus()))")
    io.swagger.client.model.CaseNew mapCaseNew(com.mytech.casemanagement.entity.CaseNew entityCaseNew);

    default CaseNew.CaseStatusEnum mapCaseStatus(com.mytech.casemanagement.entity.CaseStatus caseStatus) {
        switch (caseStatus) {
            case PendingDocument:
                return CaseNew.CaseStatusEnum.PENDINGDOCUMENT;
            case PendingReview:
                return io.swagger.client.model.CaseNew.CaseStatusEnum.PENDINGREVIEW;
            default:
                throw new IllegalArgumentException("Unknown enum value: " + caseStatus);
        }
    }*/
}