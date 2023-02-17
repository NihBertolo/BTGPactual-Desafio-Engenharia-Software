package com.nicolebertolo.msbackendforfronted.grpc.client.domain.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductRequest {

    private String identificationCode;
    private BigDecimal price;
    private String name;
    private String description;
    private ProductResponse.StockInfo stockInfo;
}
