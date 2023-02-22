package com.nicolebertolo.msproduct.exceptions.handlers;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.nicolebertolo.msproduct.exceptions.ProductNotFoundException;
import io.grpc.StatusRuntimeException;
import lombok.val;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@net.devh.boot.grpc.server.advice.GrpcAdvice
public class GrpcAdvice {

    @GrpcExceptionHandler(ProductNotFoundException.class)
    public StatusRuntimeException handleProductNotFoundException(ProductNotFoundException ex) {
        val status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage(ex.getMessage())
                .build();
        return toStatusRuntimeException(status);
    }

}
