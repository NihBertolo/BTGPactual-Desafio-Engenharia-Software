package com.nicolebertolo.msbackendforfronted.grpc.client.service;

import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msbackendforfronted.exceptions.OperationException;
import com.nicolebertolo.msbackendforfronted.exceptions.ResourceNotFoundException;
import com.nicolebertolo.msbackendforfronted.grpc.client.domain.payment.PaymentRequest;
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

@Service
public class PaymentServiceGRPC extends PaymentServiceAPIGrpc.PaymentServiceAPIImplBase {

    @Value("${grpc.clients.payment.address}")
    private String address;

    @Value("${grpc.clients.payment.port}")
    private int port;

    private ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FindPaymentByIdResponse findPaymentById(String paymentId, String tracing) throws ResourceNotFoundException {
        LOGGER.info("[PaymentServiceGRPC.findPaymentById] - Init GRPC Communication");
        try {
            val findPaymentByIdRequest = FindPaymentByIdRequest.newBuilder()
                    .setPaymentId(paymentId)
                    .setTracing(tracing)
                    .build();

            return PaymentServiceAPIGrpc.newBlockingStub(this.getChannel()).findPaymentById(findPaymentByIdRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.NOT_FOUND)) {
                throw new ResourceNotFoundException("Payment with id: " + paymentId + " not found.");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public PostPaymentByIdResponse postPayment(PaymentRequest paymentRequest, String tracing) {
        LOGGER.info("[PaymentServiceGRPC.postPayment] - Init GRPC Communication");
        try {
            val postPaymentRequest = PostPaymentByIdRequest.newBuilder()
                    .setPaymentId(paymentRequest.getId())
                    .setMethod(paymentRequest.getMethod())
                    .setStatus(paymentRequest.getStatus())
                    .setTracing(tracing)
                    .build();

            return PaymentServiceAPIGrpc.newBlockingStub(this.getChannel()).postPaymentById(postPaymentRequest);
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode().toStatus().equals(Status.NOT_FOUND)) {
                throw new ResourceNotFoundException("Payment with id: " + paymentRequest.getId() + " not found.");
            } else if (ex.getStatus().getCode().toStatus().equals(Status.INVALID_ARGUMENT)) {
                throw new IllegalArgumentException("Only payments methods allowed: PIX, CREDIT_CARD, BANKSLIP.\n " +
                        "Only payments status allowed: PENDING, REFUSED, CONFIRMED, CANCELLED.");
            } else {
                throw new OperationException("Error at communication.");
            }
        }
    }

    public FindAllPaymentsResponse findAllPayments(String tracing) {
        LOGGER.info("[PaymentServiceGRPC.findAllPayments] - Init GRPC Communication");
        try {
            val findAllPaymentsRequest = FindAllPaymentsRequest.newBuilder()
                    .setTracing(tracing)
                    .build();

            return PaymentServiceAPIGrpc.newBlockingStub(this.getChannel()).findAllPayments(findAllPaymentsRequest);
        } catch (StatusRuntimeException ex) {
            throw new OperationException("Error at communication.");
        }
    }
}
