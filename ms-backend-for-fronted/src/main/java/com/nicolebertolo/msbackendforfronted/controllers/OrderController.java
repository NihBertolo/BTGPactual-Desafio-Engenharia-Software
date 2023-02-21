package com.nicolebertolo.msbackendforfronted.controllers;

import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderRequest;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.order.OrderResponse;
import com.nicolebertolo.msbackendforfronted.services.OrderServiceAPI;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    @Autowired
    private OrderServiceAPI orderServiceAPI;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable("id") String orderId) {
        LOGGER.info("[OrderController.findOrderById] - Request controller");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.OK).body(this.orderServiceAPI.findOrderById(orderId, tracing));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        LOGGER.info("[OrderController.findOrderById] - Request controller");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderServiceAPI.createOrder(orderRequest, tracing));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        LOGGER.info("[OrderController.findOrderById] - Request controller");
        val tracing = UUID.randomUUID().toString();

        return ResponseEntity.status(HttpStatus.OK).body(this.orderServiceAPI.findAllOrders(tracing));
    }
}
