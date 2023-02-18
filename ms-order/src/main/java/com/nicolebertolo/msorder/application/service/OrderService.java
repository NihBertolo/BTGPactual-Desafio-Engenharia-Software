package com.nicolebertolo.msorder.application.service;

import com.nicolebertolo.grpc.customerapi.CustomerAddressDto;
import com.nicolebertolo.msorder.application.domain.enums.OrderStatus;
import com.nicolebertolo.msorder.application.domain.models.Order;
import com.nicolebertolo.msorder.application.domain.models.OrderDetails;
import com.nicolebertolo.msorder.application.domain.models.ProductInfo;
import com.nicolebertolo.msorder.application.ports.output.grpc.channel.CustomerChannel;
import com.nicolebertolo.msorder.application.ports.output.grpc.channel.ProductChannel;
import com.nicolebertolo.msorder.application.repository.OrderRepository;
import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.output.producer.OrderCreatedPaymentProducer;
import com.nicolebertolo.msorder.infrastructure.adapters.request.OrderRequest;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentCreatedMessage;
import com.nicolebertolo.msorder.infrastructure.adapters.request.enums.PaymentMethod;
import com.nicolebertolo.msorder.infrastructure.adapters.request.enums.PaymentStatus;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductChannel productChannel;

    @Autowired
    private CustomerChannel customerChannel;

    @Autowired
    private OrderCreatedPaymentProducer orderCreatedPaymentProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public Order findOrderById(String orderId) {
        LOGGER.info("[OrderService.findOrderById] - Finding order by id:" + orderId);
        val order = this.orderRepository.findById(orderId);
        return order.orElse(null);
    }

    public Order createOrder(OrderRequest orderRequest, String tracing) {
        LOGGER.info("[OrderService.createOrder] - Creating a new order");

        val products = new ArrayList<ProductInfo>();

        for (String productId : orderRequest.getProductsIds()) {
            val tracingStream = UUID.randomUUID().toString();
            val productResponse = this.productChannel.findProductById(productId, tracingStream);

            products.add(ProductInfo.builder()
                    .productId(productResponse.getId())
                    .price(productResponse.getPrice())
                    .productName(productResponse.getName())
                    .build());
        }

        val customer = this.customerChannel.findCustomerById(orderRequest.getCustomerId(), tracing);

        LOGGER.info("[OrderService.createOrder] - Order indexed");
        val order = this.orderRepository.insert(
                Order.builder()
                        .id(UUID.randomUUID().toString())
                        .customerId(customer.getCustomerDto().getId())
                        .details(
                                OrderDetails.builder()
                                        .totalAmount(
                                                products.stream().map(ProductInfo::getPrice)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                                        )
                                        .customerAddress(customer.getCustomerDto()
                                                .getCustomerAddressDtoList().stream()
                                                .filter(CustomerAddressDto::getIsPrincipal)
                                                .findFirst().orElse(null).getAddressName())
                                        .items(products)
                                        .build())
                        .status(OrderStatus.PENDING_PAYMENT)
                        .creationDate(LocalDateTime.now())
                        .build()
        );

        MessageTemplate<PaymentCreatedMessage> message = new MessageTemplate<>(
                "ms-order",
                tracing,
                LocalDateTime.now().toString(),
                PaymentCreatedMessage.builder()
                        .orderId(order.getId())
                        .customerId(order.getCustomerId())
                        .paymentValue(order.getDetails().getTotalAmount())
                        .paymentStatus(PaymentStatus.PENDING)
                        .additionalInfo("Order Created")
                        .build());

        orderCreatedPaymentProducer.produceMessage(message);

        LOGGER.info("[CustomerService.createOrder] - A new order with id: " + order.getId() + " has been created");
        return order;
    }

    public void handleOrderStatusById(OrderStatus orderStatus, String orderId) {
        LOGGER.info("[OrderService.handleOrderStatusById] - Updating order by id:" + orderId);
        val updateOrder = this.orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException(""));

        updateOrder.setStatus(orderStatus);

        LOGGER.info("[OrderService.handleOrderStatusById] - Order id:" + orderId + "and status: "
                + updateOrder.getStatus() + "has been updated.");

        for (ProductInfo item : updateOrder.getDetails().getItems()) {
            var initialQuantity = 0;
            if (orderStatus.equals(OrderStatus.CONFIRMED)) {
                initialQuantity = -1;
            } else if (orderStatus.equals(OrderStatus.CANCELLED)) {
                initialQuantity = 1;
            }
            val tracing = UUID.randomUUID().toString();
            val newQuantity = productChannel.handleProductQuantity(item.getProductId(), initialQuantity, tracing);

            if(newQuantity.getNewQuantity() < 0) updateOrder.setStatus(OrderStatus.CANCELLED);


            LOGGER.info("[OrderService.handleOrderStatusById] - New quantity: " +newQuantity.getNewQuantity()
                    + "for productId: " +item.getProductId());
        }

        this.orderRepository.save(updateOrder);
    }

    public List<Order> findAll() {
        LOGGER.info("[OrderService.findAll] - Finding all Orders");

        return this.orderRepository.findAll();
    }
}
