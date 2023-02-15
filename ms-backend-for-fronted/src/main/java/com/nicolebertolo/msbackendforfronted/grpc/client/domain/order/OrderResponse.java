package com.nicolebertolo.msbackendforfronted.grpc.client.domain.order;

import com.nicolebertolo.grpc.customerapi.OrderDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static OrderResponse toResponse(OrderDto orderDto) {
        return OrderResponse.builder()
                .id(orderDto.getId())
                .customerId(orderDto.getCustomerId())
                .details(OrderDetails.builder()
                        .items(
                                orderDto.getOrderDetailsDto()
                                        .getProductInfoDtoList().stream().map(productInfoDto ->
                                                OrderDetails.ProductInfo.builder()
                                                        .price(BigDecimal.valueOf(productInfoDto.getPrice()).setScale(2))
                                                        .productId(productInfoDto.getProductId())
                                                        .productName(productInfoDto.getProductName())
                                                        .build()
                                        ).collect(Collectors.toList()))
                        .customerAddress(orderDto.getOrderDetailsDto().getCustomerAddress())
                        .totalAmount(BigDecimal.valueOf(orderDto.getOrderDetailsDto().getTotalAmount()))
                        .build()
                )
                .status(orderDto.getStatus())
                .creationDate(LocalDateTime.parse(orderDto.getCreationDate()))
                .lastUpdatedDate(LocalDateTime.parse(orderDto.getLastUpdatedDate()))
                .build();
    }

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






