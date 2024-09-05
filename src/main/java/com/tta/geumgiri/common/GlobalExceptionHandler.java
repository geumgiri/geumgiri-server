package com.tta.geumgiri.common;

import lombok.extern.slf4j.Slf4j;
import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorResponse;
import com.tta.geumgiri.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
        .body(ErrorResponse.of(e.getErrorStatus().getStatus(),e.getErrorStatus().getMessage()));
  }
}
