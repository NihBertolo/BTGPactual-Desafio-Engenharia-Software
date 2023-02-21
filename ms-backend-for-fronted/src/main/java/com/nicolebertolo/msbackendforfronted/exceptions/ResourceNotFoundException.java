package com.nicolebertolo.msbackendforfronted.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String message;
}
