package com.nicolebertolo.msorder.infrastructure.adapters.request;

import com.nicolebertolo.msorder.infrastructure.adapters.request.enums.PaymentMethod;
import com.nicolebertolo.msorder.infrastructure.adapters.request.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReceivedMessage {
    private BigDecimal payedValue;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String customerId;
    private String orderId;
}
