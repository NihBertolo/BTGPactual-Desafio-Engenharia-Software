package com.nicolebertolo.msproduct.grpc.server;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msproduct.domain.models.StockInfo;
import com.nicolebertolo.msproduct.grpc.server.request.ProductRequest;
import com.nicolebertolo.msproduct.services.ProductService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.val;
import net.devh.boot.grpc.server.service.GrpcService;
import com.nicolebertolo.grpc.customerapi.ProductServiceAPIGrpc.ProductServiceAPIImplBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

@GrpcService
public class ProductGrpcService extends ProductServiceAPIImplBase {

    @Autowired
    private ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findProductById(
            FindProductByIdRequest request,
            StreamObserver<FindProductByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[ProductServer.findCustomerById] - Finding Product by Id, tracing: " + request.getTracing());

            val product = this.productService.findProductById(request.getProductId());

            val productResponse = FindProductByIdResponse.newBuilder()
                    .setProductDto(
                            ProductDto.newBuilder()
                                    .setId(product.getId())
                                    .setDescription(product.getDescription())
                                    .setName(product.getName())
                                    .setIdentificationCode(product.getIdentificationCode())
                                    .setPrice(product.getPrice().doubleValue())
                                    .setStockInfoDto(
                                            StockInfoDto.newBuilder()
                                                    .setQuantity(product.getStockInfo().getQuantity())
                                                    .setSupplierId(product.getStockInfo().getSupplierId())
                                                    .build()
                                    )
                                    .setCreationDate(product.getCreationDate().toString())
                                    .build()
                    ).build();

            responseObserver.onNext(productResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void createProduct(
            CreateProductRequest request,
            StreamObserver<CreateProductResponse> responseObserver
    ) {
        try {
            LOGGER.info("[ProductServer.createProduct] - Creating product tracing: " + request.getTracing());

            val product = this.productService.createProduct(
                    ProductRequest.builder()
                            .name(request.getName())
                            .description(request.getDescription())
                            .identificationCode(request.getIdentificationCode())
                            .stockInfo(
                                    StockInfo.builder()
                                            .quantity(request.getStockInfoDto().getQuantity())
                                            .supplierId(request.getStockInfoDto().getSupplierId()).build()
                            ).build()
            );

            val productResponse = CreateProductResponse.newBuilder()
                    .setProductDto(
                            ProductDto.newBuilder()
                                    .setId(product.getId())
                                    .setDescription(product.getDescription())
                                    .setName(product.getName())
                                    .setIdentificationCode(product.getIdentificationCode())
                                    .setPrice(product.getPrice().doubleValue())
                                    .setStockInfoDto(
                                            StockInfoDto.newBuilder()
                                                    .setQuantity(product.getStockInfo().getQuantity())
                                                    .setSupplierId(product.getStockInfo().getSupplierId())
                                                    .build()
                                    )
                                    .setCreationDate(product.getCreationDate().toString())
                                    .build()
                    ).build();

            responseObserver.onNext(productResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void findAllProducts(
            FindAllProductsRequest request,
            StreamObserver<FindAllProductsResponse> responseObserver
    ) {
        try {
            LOGGER.info("[ProductServer.findAllProducts] - Finding All Products tracing: " + request.getTracing());

            val products = this.productService.findAll();

            val productsResponse = FindAllProductsResponse.newBuilder()
                    .addAllProductDto(
                            products.stream().map(product ->
                                    ProductDto.newBuilder()
                                            .setId(product.getId())
                                            .setDescription(product.getDescription())
                                            .setName(product.getName())
                                            .setIdentificationCode(product.getIdentificationCode())
                                            .setPrice(product.getPrice().doubleValue())
                                            .setStockInfoDto(
                                                    StockInfoDto.newBuilder()
                                                            .setQuantity(product.getStockInfo().getQuantity())
                                                            .setSupplierId(product.getStockInfo().getSupplierId())
                                                            .build()
                                            )
                                            .setCreationDate(product.getCreationDate().toString())
                                            .build()
                            ).collect(Collectors.toList())
                    ).build();

            responseObserver.onNext(productsResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void handleProductQuantity(
            HandleProductQuantityRequest request,
            StreamObserver<HandleProductQuantityResponse> responseObserver
    ) {
        try {
            LOGGER.info("[ProductServer.handleProductQuantity] - Handling Product quantity tracing: " + request.getTracing());

            val product = this.productService.handleProductStockById(request.getProductId(), request.getQuantity());

            val productQuantityResponse = HandleProductQuantityResponse.newBuilder()
                    .setNewQuantity(product.getStockInfo().getQuantity())
                    .build();

            responseObserver.onNext(productQuantityResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }
}
