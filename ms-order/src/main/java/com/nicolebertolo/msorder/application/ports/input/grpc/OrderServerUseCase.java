package com.nicolebertolo.msorder.application.ports.input.grpc;

import com.nicolebertolo.grpc.customerapi.*;
import io.grpc.stub.StreamObserver;

public interface OrderServerUseCase {

    void findOrderById(FindOrderByIdRequest request, StreamObserver<FindOrderByIdResponse> responseObserver);

    void createOrder(CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseObserver);

    void findAllOrders(FindAllOrdersRequest request, StreamObserver<FindAllOrdersResponse> responseObserver);
}
