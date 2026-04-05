package com.minivest.mutualfunds.enums;

public enum TransactionType {
    LUMPSUM,                // One-time purchase of mutual fund units.
    SIP_INSTALLMENT,        // Regular, scheduled purchase of mutual fund units (e.g., monthly).
    REFUND                  // Reversal of a previous transaction, returning funds to the investor.
}
