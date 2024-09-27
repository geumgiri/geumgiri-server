package com.tta.geumgiri.common;

import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorResponse;
import com.tta.geumgiri.common.exception.NotFoundException;
import com.tta.geumgiri.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.of(e.getErrorStatus().getStatus(), e.getMessage()));
  }

  @ExceptionHandler(UnauthorizedException.class)
  protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponse.of(e.getErrorStatus().getStatus(), e.getErrorStatus().getMessage()));
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    return ResponseEntity.status(e.getErrorStatus().getStatus())
            .body(ErrorResponse.of(e.getErrorStatus().getStatus(), e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    ErrorStatus errorStatus = null;

    // 예외 메시지에 따라 적절한 ErrorStatus를 설정
    if (e.getMessage().equals("DUPLICATE_USER_ID")) {
      errorStatus = ErrorStatus.DUPLICATE_USER_ID;
    }

    assert errorStatus != null;
    return ResponseEntity.status(errorStatus.getStatus())
            .body(ErrorResponse.of(errorStatus.getStatus(), errorStatus.getMessage()));
  }
}
