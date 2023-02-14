package com.nicolebertolo.msorder.domain.enums;



public enum OrderStatus {
    PENDING_PAYMENT(1),
    PAYMENT_REFUSED(2),
    CONFIRMED(3),
    CANCELLED(4);

    OrderStatus(int code) {
    }
}
