package com.minivest.mutualfunds.entity;

import java.time.LocalDateTime;

import com.minivest.mutualfunds.enums.KycStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(name = "pan_number", unique = true)
    private String pan;

    @Column(name = "razorpay_customer_id", unique = true)
    private String razorpayCoustmerId;

    // Has bank account been verified via penny drop?
    // We store it to avoid creating duplicate customers on Razorpay.
    @Column(name = "bank_account_verified")
    private boolean bankVerified;

    @Column(name = "kyc_status")
    private KycStatus kycStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // this will run when
    // JPA lifecycle callback: runs BEFORE entity is first saved to DB.
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // JPA lifecycle callback: runs BEFORE entity is updated in DB.
    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
