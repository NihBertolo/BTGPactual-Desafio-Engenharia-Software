package com.nicolebertolo.msbackendforfronted.grpc.client.domain.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {

    private String id;
    private String identificationCode;
    private BigDecimal price;
    private String name;
    private String description;
    private StockInfo stockInfo;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdatedDate;

    @Data
    @Builder
    public static class StockInfo {
        private Integer quantity;
        private String supplierId;
    }

}
