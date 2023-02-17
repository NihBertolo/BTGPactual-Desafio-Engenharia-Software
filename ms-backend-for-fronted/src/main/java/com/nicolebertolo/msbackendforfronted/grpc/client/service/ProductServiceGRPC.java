package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductResponse.toResponse;

@Service
public class ProductServiceGRPC {

    @Value("${grpc.clients.product.address}")
    private String address;

    @Value("${grpc.clients.product.port}")
    private int port;

    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ProductResponse findProductById(String productId, String tracing) {
        LOGGER.info("[ProductServiceGRPC.findProductById] - Init GRPC Communication");
        val findProductByIdRequest = FindProductByIdRequest.newBuilder()
                .setProductId(productId)
                .setTracing(tracing)
                .build();

        return toResponse(ProductServiceAPIGrpc.newBlockingStub(this.getChannel())
                .findProductById(findProductByIdRequest).getProductDto());
    }

    public ProductResponse createProduct(ProductRequest productRequest, String tracing) {
        LOGGER.info("[ProductServiceGRPC.createProduct] - Init GRPC Communication");
        val createProductRequest = CreateProductRequest.newBuilder()
                .setName(productRequest.getName())
                .setDescription(productRequest.getDescription())
                .setIdentificationCode(productRequest.getIdentificationCode())
                .setPrice(productRequest.getPrice().doubleValue())
                .setStockInfoDto(
                        StockInfoDto.newBuilder()
                                .setQuantity(productRequest.getStockInfo().getQuantity())
                                .setSupplierId(productRequest.getStockInfo().getSupplierId())
                        .build())
                .setTracing(tracing)
                .build();

        return toResponse(ProductServiceAPIGrpc.newBlockingStub(this.getChannel())
                .createProduct(createProductRequest).getProductDto());
    }

    public List<ProductResponse> findAllProducts(String tracing) {
        LOGGER.info("[ProductServiceGRPC.findAllProducts] - Init GRPC Communication");
        val findAllProductsRequest = FindAllProductsRequest.newBuilder().setTracing(tracing).build();

        return ProductServiceAPIGrpc.newBlockingStub(this.getChannel())
                .findAllProducts(findAllProductsRequest)
                .getProductDtoList().stream().map(ProductResponse::toResponse).collect(Collectors.toList());
    }
}
