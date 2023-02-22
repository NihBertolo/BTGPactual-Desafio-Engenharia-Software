package com.nicolebertolo.msorder.application.exceptions.handlers;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.nicolebertolo.msorder.application.exceptions.OrderNotFoundException;
import io.grpc.StatusRuntimeException;
import lombok.val;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@net.devh.boot.grpc.server.advice.GrpcAdvice
public class GrpcAdvice {

    @GrpcExceptionHandler(OrderNotFoundException.class)
    public StatusRuntimeException handleProductNotFoundException(OrderNotFoundException ex) {
        val status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage(ex.getMessage())
                .build();
        return toStatusRuntimeException(status);
    }

}
