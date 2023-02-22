package com.nicolebertolo.mspayment.application.excpetions.handlers;

import com.google.rpc.Code;
import com.google.rpc.Status;
import com.nicolebertolo.mspayment.application.excpetions.CantConvertEnumException;
import com.nicolebertolo.mspayment.application.excpetions.PaymentNotFoundException;
import io.grpc.StatusRuntimeException;
import lombok.val;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

@net.devh.boot.grpc.server.advice.GrpcAdvice
public class GrpcAdvice {

    @GrpcExceptionHandler(PaymentNotFoundException.class)
    public StatusRuntimeException handleProductNotFoundException(PaymentNotFoundException ex) {
        val status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage(ex.getMessage())
                .build();
        return toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(CantConvertEnumException.class)
    public StatusRuntimeException handleCantConvertEnumException(CantConvertEnumException ex) {
        val status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage(ex.getMessage())
                .build();
        return toStatusRuntimeException(status);
    }

}
