package com.nicolebertolo.mspayment.application.domain.enums;

public enum PaymentStatus {
    PENDING(1),
    REFUSED(2),
    CONFIRMED(3),
    CANCELLED(4);

    PaymentStatus(int i) {
    }
}
