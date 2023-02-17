package com.nicolebertolo.msorder.application.ports.output.grpc.component;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomerGrpcClient {

    @Value("${grpc.clients.customer.address}")
    private static final String address = "";

    @Value("${grpc.clients.customer.port}")
    private static final int port = 0;

    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }
}
