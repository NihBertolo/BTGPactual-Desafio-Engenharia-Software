package com.nicolebertolo.msbackendforfronted.services;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.OrderServiceGRPC;
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
public class OrderServiceAPI {

    @Autowired
    private OrderServiceGRPC orderServiceGRPC;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public OrderResponse findOrderById(String orderId, String tracing) {
        val orderGRPC = this.orderServiceGRPC.findOrderById(orderId, tracing);

        LOGGER.info("[OrderServiceAPI.findOrderById] - Converting gRPC protobuf to Model, tracing: " +tracing);
        return toResponse(orderGRPC.getOrderDto());
    }

    public OrderResponse createOrder(OrderRequest orderRequest, String tracing) {
        val orderGRPC = this.orderServiceGRPC.createOrder(orderRequest, tracing);

        LOGGER.info("[OrderServiceAPI.createOrder] - Converting gRPC protobuf to Model, tracing: " +tracing);
        return toResponse(orderGRPC.getOrderDto());
    }

    public List<OrderResponse> findAllOrders(String tracing) {
        val orderGRPC = this.orderServiceGRPC.findAllOrders(tracing);

        LOGGER.info("[OrderServiceAPI.findAllOrders - Converting gRPC protobuf to Model, tracing: " +tracing);
        return orderGRPC.getOrderDtoList().stream().map(OrderResponse::toResponse).collect(Collectors.toList());
    }
}
