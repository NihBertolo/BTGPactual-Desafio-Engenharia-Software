package com.nicolebertolo.msproduct.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "product")
@Data
@Builder
public class Product {

    @Id
    @Indexed
    private String id;
    @Indexed(unique = true)
    private String identificationCode;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;
    private String name;
    private String description;
    private StockInfo stockInfo;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;
}
