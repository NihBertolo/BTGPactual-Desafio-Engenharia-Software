package com.nicolebertolo.mspayment.infrastructure.adapters.request;

import com.nicolebertolo.mspayment.application.domain.enums.PaymentMethod;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentReceivedMessage {
    private BigDecimal payedValue;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String orderId;
}
