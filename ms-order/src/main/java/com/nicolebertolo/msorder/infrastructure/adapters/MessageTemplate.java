package com.nicolebertolo.msorder.infrastructure.adapters;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageTemplate<T> {
    private String origin;
    private LocalDateTime timestamp;
    private String tracing;
    private T message;
}
