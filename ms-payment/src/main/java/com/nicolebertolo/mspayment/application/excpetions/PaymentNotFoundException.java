package com.nicolebertolo.mspayment.application.excpetions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PaymentNotFoundException extends RuntimeException{
    private String message;
}
