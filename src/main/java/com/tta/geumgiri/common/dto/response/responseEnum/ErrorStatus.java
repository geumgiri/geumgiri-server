package com.tta.geumgiri.common.dto.response.responseEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

  MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"ID에 해당하는 멤버가 존재하지 않습니다."),
  CARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"ID에 해당하는 카드가 존재하지 않습니다."),
  ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"ID에 해당하는 계좌가 존재하지 않습니다."),

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
  DUPLICATE_ACCOUNT_ID(HttpStatus.CONFLICT.value(), "이미 사용중인 계좌입니다.")
  ;

  private final int status;
  private final String message;
}
