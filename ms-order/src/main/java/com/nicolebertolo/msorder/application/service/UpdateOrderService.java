package com.nicolebertolo.msorder.application.service;


import com.nicolebertolo.msorder.application.domain.enums.OrderStatus;
import com.nicolebertolo.msorder.application.exceptions.MatchPaymentStatusException;
import com.nicolebertolo.msorder.application.ports.input.consumer.UpdateOrderServiceUseCase;
import com.nicolebertolo.msorder.infrastructure.adapters.MessageTemplate;
import com.nicolebertolo.msorder.infrastructure.adapters.request.PaymentReceivedMessage;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class UpdateOrderService implements UpdateOrderServiceUseCase {

    @Autowired
    private OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void updateOrder(MessageTemplate<PaymentReceivedMessage> payload) {

        val orderId = payload.getMessage().getOrderId();
        val paymentMethod = payload.getMessage().getPaymentMethod();
        val paymentStatus = payload.getMessage().getPaymentStatus();
        LOGGER.info("[UpdateOrderService.updateOrder] - Updating order by payload received. PAYMENT-METHOD:"
                +paymentMethod +", paymentStatus: " +paymentStatus +", orderId: " +orderId);

        switch (payload.getMessage().getPaymentStatus()) {
            case PENDING:
                this.orderService.handleOrderStatusById(OrderStatus.PENDING_PAYMENT, orderId);
                break;
            case REFUSED:
                this.orderService.handleOrderStatusById(OrderStatus.PAYMENT_REFUSED, orderId);
                break;
            case CANCELLED:
                this.orderService.handleOrderStatusById(OrderStatus.CANCELLED, orderId);
                break;
            case CONFIRMED:
                this.orderService.handleOrderStatusById(OrderStatus.CONFIRMED, orderId);
                break;
            default:
                throw new MatchPaymentStatusException("Payment Status couldn't be mapped.");
        }
    }
}
