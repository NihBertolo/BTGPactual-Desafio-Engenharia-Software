package com.nicolebertolo.mscustomer.exceptions.handlers;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class GrpcErrorHandler {

    public StatusRuntimeException handle(Exception ex) {
        val status = ex instanceof StatusRuntimeException ? Status
                .fromCode(((StatusRuntimeException) ex).getStatus().getCode())
                .withDescription(((StatusRuntimeException) ex).getStatus().getDescription()) : Status.UNKNOWN;

        return status.withCause(ex).asRuntimeException();
    }
}
