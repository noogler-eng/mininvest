package com.minivest.mutualfunds.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.minivest.mutualfunds.enums.MandateStatus;
import com.minivest.mutualfunds.enums.SipStatus;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sip_registrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SipRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "scheme_name")
    private String schemeName;

    // Monthly SIP amount. BigDecimal for money!
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    // Day of month for debit: 1–28. Don't use 29-31 (not all months have them!).
    @Column(name = "sip_date", nullable = false)
    private Integer sipDate;

    // "YYYY-MM" format. We only care about month/year for SIP start.
    @Column(name = "start_date", nullable = false)
    private String startDate; 

    // mandate linking to the SIP
    // We can have multiple SIPs linked to the same mandate if user invests in multiple schemes.
    // Razorpay order_id for the mandate registration.
    @Column(name = "mandate_order_id")
    private String mandateOrderId;

    // Razorpay token_id — THE key to charge the mandate.
    // Set when token.activated webhook fires.
    @Column(name = "mandate_token_id")
    private String mandateTokenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mandate_status")
    private MandateStatus mandateStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sip_status", nullable = false)
    private SipStatus sipStatus;

    @Column(name = "completed_installments")
    private Integer completedInstallments = 0;

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
