package com.nicolebertolo.msorder.application.ports.input.grpc;

import com.google.protobuf.ProtocolStringList;
import com.nicolebertolo.grpc.customerapi.*;
import com.nicolebertolo.msorder.application.service.OrderService;
import com.nicolebertolo.msorder.infrastructure.adapters.request.OrderRequest;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.val;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

@GrpcService
public class OrderServer extends OrderServiceAPIGrpc.OrderServiceAPIImplBase {

    @Autowired
    private OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void findOrderById(
            FindOrderByIdRequest request,
            StreamObserver<FindOrderByIdResponse> responseObserver
    ) {
        try {
            LOGGER.info("[OrderServer.findCustomerById] - Finding Order by Id, tracing: " + request.getTracing());

            val order = this.orderService.findOrderById(request.getOrderId());
            val orderResponse = FindOrderByIdResponse.newBuilder()
                    .setOrderDto(
                            OrderDto.newBuilder()
                                    .setId(order.getId())
                                    .setCustomerId(order.getCustomerId())
                                    .setOrderDetailsDto(
                                            OrderDetailsDto.newBuilder()
                                                    .setCustomerAddress(order.getDetails().getCustomerAddress())
                                                    .setTotalAmount(order.getDetails().getTotalAmount().doubleValue())
                                                    .addAllProductInfoDto(
                                                            order.getDetails().getItems().stream().map(item ->
                                                                    ProductInfoDto.newBuilder()
                                                                            .setProductId(item.getProductId())
                                                                            .setPrice(item.getPrice().doubleValue())
                                                                            .setProductName(item.getProductName())
                                                                            .build()
                                                            ).collect(Collectors.toList())

                                                    ).build())
                                    .setStatus(order.getStatus().toString())
                                    .setCreationDate(order.getCreationDate().toString())
                                    .setLastUpdatedDate(order.getLastUpdatedDate().toString())
                                    .build()
                    ).build();
            LOGGER.info("[OrderServer.findCustomerById] - Order found by Id, tracing: " + request.getTracing());

            responseObserver.onNext(orderResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void createOrder(
            CreateOrderRequest request,
            StreamObserver<CreateOrderResponse> responseObserver
    ) {
        try {
            LOGGER.info("[OrderServer.createOrder] - Creating order, tracing: " + request.getTracing());

            val orderRequest = OrderRequest.builder()
                    .customerId(request.getCustomerId())
                    .productsIds(request.getProductsIdList())
                    .build();

            val order = this.orderService.createOrder(orderRequest, request.getTracing());

            val orderResponse = CreateOrderResponse.newBuilder()
                    .setOrderDto(
                            OrderDto.newBuilder()
                                    .setId(order.getId())
                                    .setCustomerId(order.getCustomerId())
                                    .setOrderDetailsDto(
                                            OrderDetailsDto.newBuilder()
                                                    .setCustomerAddress(order.getDetails().getCustomerAddress())
                                                    .setTotalAmount(order.getDetails().getTotalAmount().doubleValue())
                                                    .addAllProductInfoDto(
                                                            order.getDetails().getItems().stream().map(item ->
                                                                    ProductInfoDto.newBuilder()
                                                                            .setProductId(item.getProductId())
                                                                            .setPrice(item.getPrice().doubleValue())
                                                                            .setProductName(item.getProductName())
                                                                            .build()
                                                            ).collect(Collectors.toList())

                                                    ).build())
                                    .setStatus(order.getStatus().toString())
                                    .setCreationDate(order.getCreationDate().toString())
                                    .setLastUpdatedDate(order.getLastUpdatedDate().toString())
                                    .build()
                    ).build();

            responseObserver.onNext(orderResponse);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }

    @Override
    public void findAllOrders(
        FindAllOrdersRequest request,
        StreamObserver<FindAllOrdersResponse> responseObserver
    ) {
        try {
        LOGGER.info("[OrderServer.findAllOrders] - finding all orders, tracing: " + request.getTracing());

        val orders = this.orderService.findAll();

        val ordersResponse = FindAllOrdersResponse.newBuilder()
                        .addAllOrderDto(
                                orders.stream().map( order ->
                                        OrderDto.newBuilder()
                                                .setId(order.getId())
                                                .setCustomerId(order.getCustomerId())
                                                .setOrderDetailsDto(
                                                        OrderDetailsDto.newBuilder()
                                                                .setCustomerAddress(order.getDetails().getCustomerAddress())
                                                                .setTotalAmount(order.getDetails().getTotalAmount().doubleValue())
                                                                .addAllProductInfoDto(
                                                                        order.getDetails().getItems().stream().map(item ->
                                                                                ProductInfoDto.newBuilder()
                                                                                        .setProductId(item.getProductId())
                                                                                        .setPrice(item.getPrice().doubleValue())
                                                                                        .setProductName(item.getProductName())
                                                                                        .build()
                                                                        ).collect(Collectors.toList())

                                                                ).build())
                                                .setStatus(order.getStatus().toString())
                                                .setCreationDate(order.getCreationDate().toString())
                                                .setLastUpdatedDate(order.getLastUpdatedDate().toString())
                                                .build()
                                ).collect(Collectors.toList()
                        )).build();

        responseObserver.onNext(ordersResponse);
        responseObserver.onCompleted();
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().equals(BAD_REQUEST)) {
                val status = Status.INVALID_ARGUMENT.withDescription(ex.getMessage());
                responseObserver.onError(status.asRuntimeException());
            }
        }
    }
}
