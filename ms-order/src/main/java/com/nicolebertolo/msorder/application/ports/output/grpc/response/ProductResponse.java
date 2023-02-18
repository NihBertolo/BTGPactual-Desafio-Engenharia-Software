package com.nicolebertolo.msorder.application.ports.output.grpc.response;

import com.nicolebertolo.grpc.customerapi.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {

    private String id;
    private String identificationCode;
    private BigDecimal price;
    private String name;
    private String description;
    private StockInfo stockInfo;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdatedDate;

    public static ProductResponse toResponse(ProductDto productDto) {
        return ProductResponse.builder()
                .id(productDto.getId())
                .description(productDto.getDescription())
                .name(productDto.getName())
                .price(BigDecimal.valueOf(productDto.getPrice()).setScale(2))
                .identificationCode(productDto.getIdentificationCode())
                .stockInfo(
                        StockInfo.builder()
                                .quantity(productDto.getStockInfoDto().getQuantity())
                                .supplierId(productDto.getStockInfoDto().getSupplierId())
                        .build()
                )
                .creationDate(LocalDateTime.parse(productDto.getCreationDate()))
                .build();
    }

    @Data
    @Builder
    public static class StockInfo {
        private Integer quantity;
        private String supplierId;
    }

}
