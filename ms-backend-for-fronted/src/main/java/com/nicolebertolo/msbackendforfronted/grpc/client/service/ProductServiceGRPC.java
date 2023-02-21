package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.exceptions.OperationException;
import com.nicolebertolo.msbackendforfronted.exceptions.ResourceNotFoundException;
import com.nicolebertolo.msbackendforfronted.exceptions.UnavailableServiceException;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.product.ProductResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
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

    public FindProductByIdResponse findProductById(String productId, String tracing) {
        try {
            LOGGER.info("[ProductServiceGRPC.findProductById] - Init GRPC Communication");
            val findProductByIdRequest = FindProductByIdRequest.newBuilder()
                    .setProductId(productId)
                    .setTracing(tracing)
                    .build();

            return ProductServiceAPIGrpc.newBlockingStub(this.getChannel()).findProductById(findProductByIdRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.NOT_FOUND)) {
                throw new ResourceNotFoundException("Product with id: " + productId + " not found.");
            } else if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public CreateProductResponse createProduct(ProductRequest productRequest, String tracing) {
        LOGGER.info("[ProductServiceGRPC.createProduct] - Init GRPC Communication");
        try {
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

            return ProductServiceAPIGrpc.newBlockingStub(this.getChannel()).createProduct(createProductRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public FindAllProductsResponse findAllProducts(String tracing) {
        LOGGER.info("[ProductServiceGRPC.findAllProducts] - Init GRPC Communication");
        try {
            val findAllProductsRequest = FindAllProductsRequest.newBuilder().setTracing(tracing).build();

            return ProductServiceAPIGrpc.newBlockingStub(this.getChannel()).findAllProducts(findAllProductsRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }
}
