package com.mytech.casemanagement.repository;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

//public interface CaseRepository extends JpaRepository<CaseString, Integer> {
public interface CaseRepository extends JpaRepository<Case, Integer> {
    Optional<Case> findByCaseId(int caseId);
/*    Optional<CaseString> findByCaseId(int caseId);*/

/*    @Query("select c from CaseString c")
    Optional<CaseString> findByCaseIdQuery(int caseId);*/

/*
    @Query(value = "select * from dbo_CASE_STRING where id=:caseId", nativeQuery = true)
    Optional<CaseString> findByCaseIdNativeQuery(int caseId);*/

}