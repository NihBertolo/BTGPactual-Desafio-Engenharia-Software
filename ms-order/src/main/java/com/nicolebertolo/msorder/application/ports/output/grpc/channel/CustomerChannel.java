package com.nicolebertolo.msorder.application.ports.output.grpc.channel;

import com.nicolebertolo.grpc.customerapi.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;


@Service
public class CustomerChannel {

    @Value("${grpc.clients.customer.address}")
    private String address;

    @Value("${grpc.clients.customer.port}")
    private int port;

    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindCustomerByIdResponse findCustomerById(String customerId, String tracing) {
        LOGGER.info("[CustomerChannel.findCustomerById] - Init GRPC Communication");
        val findCustomerByIdRequest = FindCustomerByIdRequest.newBuilder()
                .setId(customerId)
                .setTracing(tracing)
                .build();

        return CustomerServiceAPIGrpc.newBlockingStub(this.getChannel()).findCustomerById(findCustomerByIdRequest);
    }
}
