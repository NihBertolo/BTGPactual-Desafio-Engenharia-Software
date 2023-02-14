package com.nicolebertolo.msorder.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Data
@Builder
public class ProductInfo {
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
    private String productId;
    private String productName;
}
