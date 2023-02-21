package com.nicolebertolo.msproduct.grpc.server;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msproduct.domain.models.Product;
import com.nicolebertolo.msproduct.domain.models.StockInfo;
import com.nicolebertolo.msproduct.exceptions.handlers.GrpcErrorHandler;
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
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

@GrpcService
public class ProductGrpcService extends ProductServiceAPIImplBase {

    @Autowired
    private ProductService productService;

    @Autowired
    private GrpcErrorHandler grpcErrorHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findProductById(
            FindProductByIdRequest request,
            StreamObserver<FindProductByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[ProductServer.findCustomerById] - Finding Product by Id, tracing: " + request.getTracing());

            val product = this.productService.findProductById(request.getProductId());

            val productResponse = FindProductByIdResponse.newBuilder().setProductDto(toDto(product)).build();

            responseObserver.onNext(productResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
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
                            .price(BigDecimal.valueOf(request.getPrice()))
                            .identificationCode(request.getIdentificationCode())
                            .stockInfo(
                                    StockInfo.builder()
                                            .quantity(request.getStockInfoDto().getQuantity())
                                            .supplierId(request.getStockInfoDto().getSupplierId()).build()
                            ).build()
            );

            val productResponse = CreateProductResponse.newBuilder().setProductDto(toDto(product)).build();

            responseObserver.onNext(productResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
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
                    .addAllProductDto(products.stream().map(this::toDto).collect(Collectors.toList())).build();

            responseObserver.onNext(productsResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
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
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }

    private ProductDto toDto(Product product) {
        return ProductDto.newBuilder()
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
                .build();
    }
}
