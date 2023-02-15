package com.nicolebertolo.msproduct.grpc.server.request;

import com.nicolebertolo.msproduct.domain.models.StockInfo;
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
    private StockInfo stockInfo;
}
