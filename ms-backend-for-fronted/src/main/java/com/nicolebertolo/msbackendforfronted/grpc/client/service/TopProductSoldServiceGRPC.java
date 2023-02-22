package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.FindTopProductsSoldRequest;
import com.nicolebertolo.grpc.customerapi.FindTopProductsSoldResponse;
import com.nicolebertolo.grpc.customerapi.ProductCacheAPIGrpc;
import com.nicolebertolo.msbackendforfronted.exceptions.OperationException;
import com.nicolebertolo.msbackendforfronted.exceptions.UnavailableServiceException;
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

@Service
public class TopProductSoldServiceGRPC {

    @Value("${grpc.clients.product.address}")
    private String address;

    @Value("${grpc.clients.product.port}")
    private int port;

    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindTopProductsSoldResponse findTopProductsSold(String tracing) {
        try {
            LOGGER.info("[TopProductSoldServiceGRP.findTopProductsSold] - Init GRPC Communication");

            val findTopProductsRequest = FindTopProductsSoldRequest.newBuilder().setTracing(tracing).build();

            return ProductCacheAPIGrpc.newBlockingStub(this.getChannel()).findTopProductsSold(findTopProductsRequest);

        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }
}
