package com.nicolebertolo.msorder.infrastructure.adapters.request;

import com.nicolebertolo.msorder.application.domain.enums.OrderStatus;
import com.nicolebertolo.msorder.application.domain.models.OrderDetails;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequest {
    private String customerId;
    private List<String> productsIds;
}
