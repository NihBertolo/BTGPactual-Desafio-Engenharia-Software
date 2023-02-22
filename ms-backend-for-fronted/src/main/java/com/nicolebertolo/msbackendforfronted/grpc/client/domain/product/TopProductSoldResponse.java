package com.nicolebertolo.msbackendforfronted.grpc.client.domain.product;

import com.nicolebertolo.grpc.customerapi.TopProductSoldDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopProductSoldResponse {
    private String id;
    private ProductResponse product;
    private Long quantitySold;

    public static TopProductSoldResponse toResponse (TopProductSoldDto topProductSoldDto) {
        return TopProductSoldResponse.builder()
                .id(topProductSoldDto.getId())
                .product(ProductResponse.toResponse(topProductSoldDto.getProductDto()))
                .quantitySold((long) topProductSoldDto.getQuantitySold())
                .build();
    }
}
