package com.nicolebertolo.msproduct.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockInfo {
    private Integer quantity;
    private String supplierId;
}
