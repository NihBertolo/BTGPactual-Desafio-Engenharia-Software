package com.nicolebertolo.msproduct.grpc.client;

import com.nicolebertolo.grpc.customerapi.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class OrderServiceGRPC extends OrderServiceAPIGrpc.OrderServiceAPIImplBase {

    @Value("${grpc.clients.order.address}")
    private String address;

    @Value("${grpc.clients.order.port}")
    private int port;

    private ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindAllOrdersResponse findAllOrders(String tracing) {
        LOGGER.info("[OrderServiceGRPC.findAllOrders] - Init GRPC Communication");

        val findAllOrdersRequest = FindAllOrdersRequest.newBuilder().setTracing(tracing).build();

        return OrderServiceAPIGrpc.newBlockingStub(this.getChannel()).findAllOrders(findAllOrdersRequest);

    }
}
