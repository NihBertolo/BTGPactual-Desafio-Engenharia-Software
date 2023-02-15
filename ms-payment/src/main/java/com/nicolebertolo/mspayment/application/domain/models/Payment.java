package com.nicolebertolo.mspayment.application.domain.models;

import com.nicolebertolo.mspayment.application.domain.enums.PaymentMethod;
import com.nicolebertolo.mspayment.application.domain.enums.PaymentStatus;
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

@Document(collation = "payment")
@Data
@Builder
public class Payment {
    @Id
    @Indexed
    private String id;
    @Indexed
    private String orderId;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String additionalInfo;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;

}
