package com.nicolebertolo.msproduct.domain.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@RedisHash(value = "TopProduct", timeToLive = 60*60*48)
@Data
@Builder
public class TopProductSeller {
    private String id;
    private Product product;
    private Long quantitySold;
}
