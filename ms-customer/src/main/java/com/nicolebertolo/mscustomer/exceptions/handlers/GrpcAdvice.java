package com.nicolebertolo.mscustomer.exceptions.handlers;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.nicolebertolo.mscustomer.exceptions.CustomerNotFoundException;
import io.grpc.StatusRuntimeException;
import lombok.val;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@net.devh.boot.grpc.server.advice.GrpcAdvice
public class GrpcAdvice {

    @GrpcExceptionHandler(CustomerNotFoundException.class)
    public StatusRuntimeException handleProductNotFoundException(CustomerNotFoundException ex) {
        val status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage(ex.getMessage())
                .build();
        return toStatusRuntimeException(status);
    }

}
