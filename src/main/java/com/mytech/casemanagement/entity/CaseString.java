package com.mytech.casemanagement.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.security.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "dbo_CASE_STRING")
@Data
public class CaseString {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "case_id", unique = true, nullable = false)
    private int caseId;

    @Column(name = "case_status", nullable = false)
    private String caseStatus;

    @Column(name = "case_type", nullable = false)
    private String caseType;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "pending_review_date")
    private LocalDateTime pendingReviewDate;

    @Column(name = "note", length = 1000)
    private String note;

    // Getters and Setters
    // ...
}

