package com.nicolebertolo.mspayment.infrastructure.adapters.request;

import com.nicolebertolo.mspayment.application.domain.enums.PaymentMethod;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequest {
    private String orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String additionalInfo;
}
