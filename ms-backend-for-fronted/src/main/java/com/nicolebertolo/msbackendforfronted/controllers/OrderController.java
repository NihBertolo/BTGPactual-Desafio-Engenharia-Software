package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse;
import com.nicolebertolo.msbackendforfronted.grpc.client.service.OrderServiceGRPC;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private OrderServiceGRPC orderServiceGRPC;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/{id}")
    public OrderResponse findOrderById(@PathVariable("id") String orderId) {
        val tracing = UUID.randomUUID().toString();

        return this.orderServiceGRPC.findOrderById(orderId, tracing);
    }

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        val tracing = UUID.randomUUID().toString();

        return this.orderServiceGRPC.createOrder(orderRequest, tracing);
    }

    @GetMapping
    public List<OrderResponse> findALl() {
        val tracing = UUID.randomUUID().toString();

        return this.orderServiceGRPC.findAllOrders(tracing);
    }
}
