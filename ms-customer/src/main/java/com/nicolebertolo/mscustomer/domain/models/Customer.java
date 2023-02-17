package com.nicolebertolo.mscustomer.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "customer")
@Data
@Builder
public class Customer {
    @Id
    @Indexed(unique = true)
    private String id;
    private String name;
    private String lastname;
    private List<CustomerDocument> documents;
    private List<Address> addresses;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;
}