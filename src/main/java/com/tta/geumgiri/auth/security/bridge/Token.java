package com.tta.geumgiri.auth.security.bridge;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record Token(
    String accessToken,
    String refreshToken
) {
  public static Token of(String accessToken, String refreshToken){
    return Token.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

}
