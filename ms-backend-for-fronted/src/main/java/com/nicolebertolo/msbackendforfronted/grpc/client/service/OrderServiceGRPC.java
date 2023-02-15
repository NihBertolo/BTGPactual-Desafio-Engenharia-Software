package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.CreateOrderRequest;
import com.nicolebertolo.grpc.customerapi.FindAllOrdersRequest;
import com.nicolebertolo.grpc.customerapi.FindOrderByIdRequest;
import com.nicolebertolo.grpc.customerapi.OrderServiceAPIGrpc;
import com.nicolebertolo.msbackendforfronted.grpc.client.component.OrderGrpcClient;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse;
import io.grpc.ManagedChannel;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import static com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse.toResponse;

@Service
public class OrderServiceGRPC extends OrderServiceAPIGrpc.OrderServiceAPIImplBase {

    @Autowired
    private OrderGrpcClient orderGrpcClient;

    private ManagedChannel channel = this.orderGrpcClient.getChannel();

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public OrderResponse findOrderById(String orderId, String tracing) {
        LOGGER.info("[OrderServiceGRPC.findOrderById] - Init GRPC Communication");
        val findOrderByIdRequest = FindOrderByIdRequest.newBuilder()
                .setOrderId(orderId)
                .setTracing(tracing)
                .build();

        return toResponse(OrderServiceAPIGrpc.newBlockingStub(channel).findOrderById(findOrderByIdRequest).getOrderDto());
    }

    public OrderResponse createOrder(OrderRequest orderRequest, String tracing) {
        LOGGER.info("[OrderServiceGRPC.createOrder] - Init GRPC Communication");
        val createOrderRequest = CreateOrderRequest.newBuilder()
                .setCustomerId(orderRequest.getCustomerId())
                .addAllProductsId(orderRequest.getProductsIds())
                .setTracing(tracing)
                .build();

        return toResponse(OrderServiceAPIGrpc.newBlockingStub(channel).createOrder(createOrderRequest).getOrderDto());
    }

    public List<OrderResponse> findAllOrders(String tracing) {
        LOGGER.info("[OrderServiceGRPC.findAllOrders] - Init GRPC Communication");
        val findAllOrdersRequest = FindAllOrdersRequest.newBuilder().setTracing(tracing).build();

        return OrderServiceAPIGrpc.newBlockingStub(channel).findAllOrders(findAllOrdersRequest)
                .getOrderDtoList().stream().map(OrderResponse::toResponse).collect(Collectors.toList());
    }
}
