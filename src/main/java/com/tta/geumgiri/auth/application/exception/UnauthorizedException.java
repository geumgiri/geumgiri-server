package com.tta.geumgiri.auth.application.exception;

import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.BusinessException;

public class UnauthorizedException extends BusinessException {

  public UnauthorizedException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
