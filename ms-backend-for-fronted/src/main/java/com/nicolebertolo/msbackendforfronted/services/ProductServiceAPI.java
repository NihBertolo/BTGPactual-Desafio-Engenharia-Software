package com.nicolebertolo.msbackendforfronted.services;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.ProductServiceGRPC;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductResponse.toResponse;

@Service
public class ProductServiceAPI {

    @Autowired
    private ProductServiceGRPC productServiceGRPC;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ProductResponse findProductById(String productId, String tracing) {
        val productGRPC = this.productServiceGRPC.findProductById(productId, tracing);
        LOGGER.info("[ProductServiceAPI.findProductById] - Converting gRPC protobuf to Model, tracing: " +tracing);

        return toResponse(productGRPC.getProductDto());
    }

    public ProductResponse createProduct(ProductRequest productRequest, String tracing) {
        val productGRPC = this.productServiceGRPC.createProduct(productRequest, tracing);
        LOGGER.info("[ProductServiceAPI.createProduct] - Converting gRPC protobuf to Model, tracing: " +tracing);

        return toResponse(productGRPC.getProductDto());
    }

    public List<ProductResponse> findAllProducts(String tracing) {
        val productGRPC = this.productServiceGRPC.findAllProducts(tracing);
        LOGGER.info("[ProductServiceAPI.findAllProducts] - Converting gRPC protobuf to Model, tracing: " +tracing);

        return productGRPC.getProductDtoList().stream().map(ProductResponse::toResponse).collect(Collectors.toList());
    }
}
