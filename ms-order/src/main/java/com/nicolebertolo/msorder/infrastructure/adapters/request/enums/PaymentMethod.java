package com.nicolebertolo.msorder.infrastructure.adapters.request.enums;

public enum PaymentMethod {
    PIX(1),
    CREDIT_CARD(2),
    BANKSLIP(3);

    PaymentMethod(int code) {
    }
}
