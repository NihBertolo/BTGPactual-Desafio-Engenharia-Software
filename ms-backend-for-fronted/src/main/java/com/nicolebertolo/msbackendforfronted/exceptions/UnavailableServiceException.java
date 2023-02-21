package com.nicolebertolo.msbackendforfronted.exceptions;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnavailableServiceException extends RuntimeException {
    private String message;
}
