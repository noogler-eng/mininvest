package com.minivest.mutualfunds.enums;

public enum TransactionStatus {
    CREATED,                // order created, waiting for payment
    PROCESSING,             // payment initiated (especially for NACH — takes days)
    SUCCESS,                // payment captured and confirmed
    FAILED,                 // payment failed (declined, insufficient funds, timeout)
    REFUNDED                // payment was refunded back to investor
}