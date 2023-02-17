package com.nicolebertolo.msorder.infrastructure.adapters.request;

import com.nicolebertolo.msorder.infrastructure.adapters.request.enums.PaymentMethod;
import com.nicolebertolo.msorder.infrastructure.adapters.request.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentCreatedMessage {
    private BigDecimal paymentValue;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String additionalInfo;
    private String customerId;
    private String orderId;
}
