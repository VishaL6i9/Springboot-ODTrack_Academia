package com.academicaffairs.odtrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "od_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ODRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @Column(name = "od_date", nullable = false)
    private LocalDate date;
    
    @ElementCollection
    @CollectionTable(name = "od_request_periods", joinColumns = @JoinColumn(name = "od_request_id"))
    @Column(name = "period")
    private List<Integer> periods;
    
    @Column(nullable = false, length = 1000)
    private String reason;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ODStatus status = ODStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private User staff;
    
    @Column(name = "signed_pdf_path")
    private String signedPdfPath;
    
    @Column(name = "rejection_reason")
    private String rejectionReason;
    
    @Column(name = "attachment_path")
    private String attachmentPath;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    public enum ODStatus {
        PENDING, APPROVED, REJECTED
    }
}