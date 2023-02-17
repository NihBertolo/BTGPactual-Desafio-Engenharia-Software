package com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

    private String id;
    private String status;
    private String method;
}
