package com.nicolebertolo.msbackendforfronted.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OperationException extends RuntimeException {
    private String message;
}
