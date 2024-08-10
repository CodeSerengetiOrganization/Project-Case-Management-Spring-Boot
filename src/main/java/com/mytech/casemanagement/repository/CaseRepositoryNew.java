package com.mytech.casemanagement.repository;

import com.mytech.casemanagement.entity.Case;
import com.mytech.casemanagement.entity.CaseNew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CaseRepositoryNew extends JpaRepository<CaseNew, Integer> {
    Optional<CaseNew> findByCaseId(int caseId);
}