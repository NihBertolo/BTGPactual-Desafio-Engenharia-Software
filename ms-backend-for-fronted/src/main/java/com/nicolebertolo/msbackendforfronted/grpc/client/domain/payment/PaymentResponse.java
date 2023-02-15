package com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private String id;
    private String orderId;
    private BigDecimal amount;
    private String status;
    private String method;
    private String additionalInfo;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdatedDate;

}
