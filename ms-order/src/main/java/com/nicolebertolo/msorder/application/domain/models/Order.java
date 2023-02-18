package com.nicolebertolo.msorder.application.domain.models;

import com.nicolebertolo.msorder.application.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "order")
@Data
@Builder
public class Order {

    @Id
    @Indexed(unique = true)
    private String id;
    @Indexed
    private String customerId;
    private OrderDetails details;
    private OrderStatus status;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdatedDate;
}
