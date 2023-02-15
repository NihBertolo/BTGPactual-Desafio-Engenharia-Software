package com.nicolebertolo.msbackendforfronted.grpc.client.domain.order;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private String id;
    @Indexed
    private String customerId;
    private OrderDetails details;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdatedDate;

    @Data
    @Builder
    public static class OrderDetails {
        private BigDecimal totalAmount;
        private String customerAddress;
        private List<ProductInfo> items;

        @Data
        @Builder
        public static class ProductInfo {
            private BigDecimal price;
            private String productId;
            private String productName;
        }
    }
}






