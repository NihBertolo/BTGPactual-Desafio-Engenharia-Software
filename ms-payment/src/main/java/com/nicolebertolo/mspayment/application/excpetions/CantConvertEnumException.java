package com.nicolebertolo.mspayment.application.excpetions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CantConvertEnumException extends RuntimeException{
    private String message;
}
