package com.nicolebertolo.msorder.application.ports.output.grpc.channel;

import com.nicolebertolo.grpc.customerapi.FindProductByIdRequest;
import com.nicolebertolo.grpc.customerapi.HandleProductQuantityRequest;
import com.nicolebertolo.grpc.customerapi.HandleProductQuantityResponse;
import com.nicolebertolo.grpc.customerapi.ProductServiceAPIGrpc;
import com.nicolebertolo.msorder.application.ports.output.grpc.response.ProductResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

import static com.nicolebertolo.msorder.application.ports.output.grpc.response.ProductResponse.toResponse;

@Service
public class ProductChannel {

    @Value("${grpc.clients.product.address}")
    private static final String address = "";

    @Value("${grpc.clients.product.port}")
    private static final int port = 0;

    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public ProductResponse findProductById(String productId, String tracing) {
        LOGGER.info("[ProductChannel.findProductById] - Init GRPC Communication");
        val findProductByIdRequest = FindProductByIdRequest.newBuilder()
                .setProductId(productId)
                .setTracing(tracing)
                .build();

        return toResponse(ProductServiceAPIGrpc.newBlockingStub(this.getChannel())
                .findProductById(findProductByIdRequest).getProductDto());
    }

    public HandleProductQuantityResponse handleProductQuantity(String productId, Integer quantity, String tracing) {
        LOGGER.info("[ProductChannel.handleProductQuantity] - Init GRPC Communication");

        val handleProductQuantityRequest = HandleProductQuantityRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .setTracing(tracing)
                .build();

        return ProductServiceAPIGrpc.newBlockingStub(this.getChannel()).handleProductQuantity(handleProductQuantityRequest);
    }
}
