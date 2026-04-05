package com.minivest.mutualfunds.enums;

public enum SipStatus {
    PENDING,            // SIP created, waiting for mandate activation
    ACTIVE,             // mandate active, SIP is running
    PAUSED,             // temporarily stopped
    CANCELLED,          // permanently stopped by user
    COMPLETED           // all installments done
}
