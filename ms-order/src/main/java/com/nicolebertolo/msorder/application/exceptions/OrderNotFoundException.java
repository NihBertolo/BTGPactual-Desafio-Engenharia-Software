package com.nicolebertolo.msorder.application.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderNotFoundException extends RuntimeException{
    private String message;
}
