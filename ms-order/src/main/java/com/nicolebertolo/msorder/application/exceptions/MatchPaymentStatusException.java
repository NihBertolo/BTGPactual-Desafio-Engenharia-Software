package com.nicolebertolo.msorder.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
public class MatchPaymentStatusException extends RuntimeException{
    private String message;
}
