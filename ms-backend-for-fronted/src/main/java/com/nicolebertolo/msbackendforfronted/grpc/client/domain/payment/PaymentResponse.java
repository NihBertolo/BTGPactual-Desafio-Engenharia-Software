package com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment;

import com.nicolebertolo.grpc.customerapi.PaymentDto;
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

    public static PaymentResponse toResponse(PaymentDto paymentDto) {
        return PaymentResponse.builder()
                .id(paymentDto.getId())
                .additionalInfo(paymentDto.getAdditionalInfo())
                .amount(BigDecimal.valueOf(paymentDto.getAmount()).setScale(2))
                .method(paymentDto.getMethod())
                .status(paymentDto.getStatus())
                .creationDate(LocalDateTime.parse(paymentDto.getCreationDate()))
                .lastUpdatedDate(LocalDateTime.parse(paymentDto.getCreationDate()))
                .build();
    }

}
