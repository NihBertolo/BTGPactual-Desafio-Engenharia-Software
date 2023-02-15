package com.nicolebertolo.msorder.application.service;

import com.nicolebertolo.msorder.application.domain.enums.OrderStatus;
import com.nicolebertolo.msorder.application.domain.models.Order;
import com.nicolebertolo.msorder.application.repository.OrderRepository;
import com.nicolebertolo.msorder.infrastructure.adapters.request.OrderRequest;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public Order findOrderById(String orderId) {
        LOGGER.info("[OrderService.findOrderById] - Finding order by id:" +orderId);
        val order = this.orderRepository.findById(orderId);
        return  order.orElse(null);
    }

    public Order createOrder(OrderRequest orderRequest) {
        LOGGER.info("[OrderService.createOrder] - Creating a new order");
        val order = this.orderRepository.insert(
                Order.builder()
                        .id(UUID.randomUUID().toString())
                        .customerId(orderRequest.getCustomerId())
                        .details(orderRequest.getDetails())
                        .status(OrderStatus.PENDING_PAYMENT)
                        .build()
        );


        LOGGER.info("[CustomerService.createOrder] - A new order with id: " + order.getId() + " has been created");
        return order;
    }

    public Order handleOrderStatusById(OrderStatus orderStatus, String orderId) {
        LOGGER.info("[OrderService.handleOrderStatusById] - Updating order by id:" +orderId);
        val updateOrder = this.orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException(""));

        updateOrder.setStatus(orderStatus);
        LOGGER.info("[OrderService.handleOrderStatusById] - Order id:" +orderId +"and status: "
                +updateOrder.getStatus() + "has been updated.");
        return this.orderRepository.save(updateOrder);
    }

    public List<Order> findAll() {
        LOGGER.info("[OrderService.findAll] - Finding all Orders");

        return this.orderRepository.findAll();
    }
}
