package com.nicolebertolo.mspayment.infrastructure.adapters.request;

import com.nicolebertolo.mspayment.application.domain.enums.PaymentMethod;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreatedMessage {
    private BigDecimal paymentValue;
    private String paymentStatus;
    private String paymentMethod;
    private String additionalInfo;
    private String customerId;
    private String orderId;
}
