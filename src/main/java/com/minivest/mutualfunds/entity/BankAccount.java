package com.minivest.mutualfunds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // connecting many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // this is branch specific
    // branch it is unique but same account number can exist in different branches.
    // varied lengths across banks, so we won't enforce length here.
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    // helps in identifying the bank branch for mandate processing
    // ifsc = BANK0001234 (4 letter bank code + 0 + 6 digit branch code)
    @Column(name = "ifsc", nullable = false, length = 11)
    private String ifsc;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(name = "verified")
    @Builder.Default
    // Penny drop verified? Name matched with KYC?
    private Boolean verified = false;

    // Payments/mandates default to primary account.
    @Column(name = "primary_account")
    @Builder.Default
    private Boolean primaryAccount = false;
}
