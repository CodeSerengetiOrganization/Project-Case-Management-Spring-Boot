package com.mytech.casemanagement.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;


@Entity
@Table(name = "dbo_CASE_NEW")
@Data
public class CaseNew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_status", nullable = false)
    private CaseStatus caseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", nullable = false)
    private CaseType caseType;

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

