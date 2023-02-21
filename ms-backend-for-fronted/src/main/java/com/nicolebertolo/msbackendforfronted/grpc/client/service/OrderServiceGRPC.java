package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.exceptions.OperationException;
import com.nicolebertolo.msbackendforfronted.exceptions.ResourceNotFoundException;
import com.nicolebertolo.msbackendforfronted.exceptions.UnavailableServiceException;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse.toResponse;

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

    public FindOrderByIdResponse findOrderById(String orderId, String tracing) {
        LOGGER.info("[OrderServiceGRPC.findOrderById] - Init GRPC Communication");
        try {
            val findOrderByIdRequest = FindOrderByIdRequest.newBuilder()
                    .setOrderId(orderId)
                    .setTracing(tracing)
                    .build();

            return OrderServiceAPIGrpc.newBlockingStub(this.getChannel()).findOrderById(findOrderByIdRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.NOT_FOUND)) {
                throw new ResourceNotFoundException("Order with id: " + orderId + " not found.");
            } else if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public CreateOrderResponse createOrder(OrderRequest orderRequest, String tracing) {
        LOGGER.info("[OrderServiceGRPC.createOrder] - Init GRPC Communication");
        try {
            val createOrderRequest = CreateOrderRequest.newBuilder()
                    .setCustomerId(orderRequest.getCustomerId())
                    .addAllProductsId(orderRequest.getProductsIds())
                    .setTracing(tracing)
                    .build();

            return OrderServiceAPIGrpc.newBlockingStub(this.getChannel()).createOrder(createOrderRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public FindAllOrdersResponse findAllOrders(String tracing) {
        LOGGER.info("[OrderServiceGRPC.findAllOrders] - Init GRPC Communication");
        try {
            val findAllOrdersRequest = FindAllOrdersRequest.newBuilder().setTracing(tracing).build();

            return OrderServiceAPIGrpc.newBlockingStub(this.getChannel()).findAllOrders(findAllOrdersRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.UNAVAILABLE)) {
                throw new UnavailableServiceException("Service unavailable");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }
}
