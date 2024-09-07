package com.tta.geumgiri.common.exception;

import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;

public class NotFoundException extends BusinessException{

  public NotFoundException(ErrorStatus errorStatus) {
    super(errorStatus);
  }
}
