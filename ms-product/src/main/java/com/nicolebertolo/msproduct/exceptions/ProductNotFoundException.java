package com.nicolebertolo.msproduct.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductNotFoundException extends RuntimeException{

    private String message;
}
