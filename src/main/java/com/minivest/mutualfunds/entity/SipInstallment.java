package com.minivest.mutualfunds.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.transaction.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sip_installments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SipInstallment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sip_id", nullable = false)
    private SipRegistration sip;

    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber; 

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private TransactionStatus paymentStatus;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "retry_count")
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { 
        createdAt = updatedAt = LocalDateTime.now(); 
    }

    @PreUpdate
    protected void onUpdate() { 
        updatedAt = LocalDateTime.now();
    }
}
