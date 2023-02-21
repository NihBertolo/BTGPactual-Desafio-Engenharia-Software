package com.nicolebertolo.msproduct.grpc.server;

import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import com.nicolebertolo.msproduct.exceptions.ProductNotFoundException;
import io.grpc.StatusRuntimeException;
import lombok.val;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@net.devh.boot.grpc.server.advice.GrpcAdvice
public class GrpcAdvice {

    @GrpcExceptionHandler
    public StatusRuntimeException handleProductNotFoundException(ProductNotFoundException prdctEx) {
        val errorInfo = ErrorInfo.newBuilder().setReason(prdctEx.getCause().getMessage()).setDomain("Product").build();

        val status = Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage(prdctEx.getMessage()).build();

        return toStatusRuntimeException(status);
    }

}
