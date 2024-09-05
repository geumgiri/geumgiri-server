package com.tta.geumgiri.auth.presentation.dto.response;

public record MemberAuthServiceResponse(
    String accessToken,
    String refreshToken
) {
  public static MemberAuthServiceResponse of(String accessToken, String refreshToken) {
    return new MemberAuthServiceResponse(accessToken, refreshToken);
  }
}
