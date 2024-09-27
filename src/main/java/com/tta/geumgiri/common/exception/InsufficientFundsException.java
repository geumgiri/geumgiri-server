package com.tta.geumgiri.common.exception;

import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
    }
}