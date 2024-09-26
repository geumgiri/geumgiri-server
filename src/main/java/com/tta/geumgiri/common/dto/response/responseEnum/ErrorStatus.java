package com.tta.geumgiri.common.dto.response.responseEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"ID에 해당하는 멤버가 존재하지 않습니다."),

  /**
   * 인증 / 인가 관련 오류
   */
  JWT_UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED.value(), "사용자의 로그인 검증을 실패했습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "올바르지 않은 리프레시 토큰입니다."),
  EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요."),
  INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(), "올바르지 않은 액세스 토큰입니다."),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED.value(), "아이디 또는 비밀번호가 올바르지 않습니다."),
  EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(),"만료된 액세스 토큰입니다."),
  UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED.value(),"접근이 허가되지 않은 유저입니다."),

  /**
   * 회원가입 관련 오류
   */
  DUPLICATE_USER_ID(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디입니다."),

  /**
   *  대출 관련 오류
   */
  LOAN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "대출이 존재하지 않습니다."),
  INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST.value(), "신용등급이 낮아 대출이 불가능합니다."),
  ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Account not found"),
  INVALID_ACCOUNT_OWNER(HttpStatus.BAD_REQUEST.value(), "이 계좌는 해당 회원의 것이 아닙니다."),
  INVALID_REPAYMENT_DATE(HttpStatus.BAD_REQUEST.value(), "상환 날짜는 오늘보다 과거일 수 없습니다."),
  INVALID_INSTALLMENT(HttpStatus.BAD_REQUEST.value(), "상환 횟수는 1 이상이어야 합니다."),
  INSTALLMENT_AMOUNT_EXCEEDS_REMAINING(HttpStatus.BAD_REQUEST.value(), "상환 금액이 남은 금액보다 큽니다."),
  INSTALLMENT_COUNT_ZERO(HttpStatus.BAD_REQUEST.value(), "상환 횟수가 0입니다. 올바른 값을 입력해야 합니다."),
  LOAN_ALREADY_PAID(HttpStatus.BAD_REQUEST.value(), "대출이 이미 상환 완료되었습니다.")
  ;

  private final int status;
  private final String message;
}
