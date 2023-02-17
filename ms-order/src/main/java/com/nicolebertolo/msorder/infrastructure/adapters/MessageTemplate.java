package com.nicolebertolo.msorder.infrastructure.adapters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MessageTemplate<T> {
    private String origin;
    private String tracing;
    private LocalDateTime timestamp;
    private T message;
}
