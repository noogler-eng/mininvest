package com.minivest.mutualfunds.enums;

public enum MandateStatus {
    CREATED,            // mandate order created on Razorpay
    AUTHORIZED,         // user completed authentication (OTP/PIN)
    CONFIRMED,          // submitted to NPCI for processing
    ACTIVATED,          // bank approved — ready for debits!
    REJECTED,           // bank rejected the mandate
    PAUSED,             // temporarily paused (by user or bank)
    CANCELLED           // permanently cancelled
}
