package com.nicolebertolo.msbackendforfronted.grpc.client.component;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductGrpcClient {

    @Value("${grpc.clients.product.address}")
    private static final String address = "";

    @Value("${grpc.clients.product.port}")
    private static final int port = 0;

    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }
}
