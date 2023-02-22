package com.nicolebertolo.msproduct.grpc.server;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msproduct.exceptions.handlers.GrpcErrorHandler;
import com.nicolebertolo.msproduct.services.TopProductService;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.val;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

@GrpcService
public class ProductCacheGrpcService extends ProductCacheAPIGrpc.ProductCacheAPIImplBase {

    @Autowired
    private TopProductService topProductService;

    @Autowired
    private GrpcErrorHandler grpcErrorHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findTopProductsSold(
            FindTopProductsSoldRequest request,
            StreamObserver<FindTopProductsSoldResponse> responseObserver
    ) {
        try {
            LOGGER.info("[ProductCacheGrpcService.findTopProductsSold] - Start finding top sold products, " +
                    "tracing: " + request.getTracing());

            var cachedProducts = this.topProductService.getCache(request.getTracing());

            if (cachedProducts.isEmpty()) {
                cachedProducts = this.topProductService.saveCache(request.getTracing());
            }

            LOGGER.info("[ProductCacheGrpcService.findTopProductsSold] - End finding top sold products, " +
                    "tracing: " + request.getTracing() + " number of elements: " + cachedProducts.size());

            val findTopProductsResponse = FindTopProductsSoldResponse.newBuilder()
                    .addAllTopProductSoldDto(cachedProducts.stream().map(cachedProduct ->
                            TopProductSoldDto.newBuilder()
                                    .setId(cachedProduct.getId())
                                    .setProductDto(
                                            ProductDto.newBuilder()
                                                    .setId(cachedProduct.getProduct().getId())
                                                    .setName(cachedProduct.getProduct().getName())
                                                    .setDescription(cachedProduct.getProduct().getDescription())
                                                    .setPrice(cachedProduct.getProduct().getPrice().doubleValue())
                                                    .setStockInfoDto(
                                                            StockInfoDto.newBuilder()
                                                                    .setQuantity(cachedProduct.getProduct().getStockInfo().getQuantity())
                                                                    .setSupplierId(cachedProduct.getProduct().getStockInfo().getSupplierId())
                                                                    .build())
                                                    .setCreationDate(cachedProduct.getProduct().getCreationDate().toString())
                                                    .build()
                                    )
                                    .setQuantitySold(cachedProduct.getQuantitySold().intValue())
                                    .build()).collect(Collectors.toList()))
                    .build();
            responseObserver.onNext(findTopProductsResponse);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            responseObserver.onError(grpcErrorHandler.handle(ex));
        }
    }
}
