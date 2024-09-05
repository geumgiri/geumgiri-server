package com.tta.geumgiri.common.exception;

import lombok.Getter;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;

@Getter
public class BusinessException extends RuntimeException{

  private final ErrorStatus errorStatus;

  public BusinessException(ErrorStatus errorStatus) {
    super(errorStatus.getMessage());
    this.errorStatus = errorStatus;
  }
}
