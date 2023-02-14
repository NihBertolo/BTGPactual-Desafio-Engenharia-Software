package com.nicolebertolo.msorder.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderDetails {
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalAmount;
    private String customerAddress;
    private List<ProductInfo> items;
}
