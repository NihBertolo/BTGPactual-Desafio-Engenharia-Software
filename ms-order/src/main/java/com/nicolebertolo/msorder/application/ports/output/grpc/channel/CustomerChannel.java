package com.nicolebertolo.msorder.application.ports.output.grpc.channel;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msorder.application.ports.output.grpc.component.CustomerGrpcClient;
import com.nicolebertolo.msorder.application.ports.output.grpc.component.ProductGrpcClient;
import com.nicolebertolo.msorder.application.ports.output.grpc.response.ProductResponse;
import io.grpc.ManagedChannel;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

import static com.nicolebertolo.msorder.application.ports.output.grpc.response.ProductResponse.toResponse;

@Service
public class CustomerChannel {

    @Autowired
    private CustomerGrpcClient customerGrpcClient;
    private ManagedChannel channel = this.customerGrpcClient.getChannel();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindCustomerByIdResponse findCustomerById(String customerId, String tracing) {
        LOGGER.info("[CustomerChannel.findCustomerById] - Init GRPC Communication");
        val findCustomerByIdRequest = FindCustomerByIdRequest.newBuilder()
                .setId(customerId)
                .setTracing(tracing)
                .build();

        return CustomerServiceAPIGrpc.newBlockingStub(channel).findCustomerById(findCustomerByIdRequest);
    }
}
