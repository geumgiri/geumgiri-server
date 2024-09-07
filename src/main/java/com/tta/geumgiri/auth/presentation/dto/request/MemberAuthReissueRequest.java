package com.tta.geumgiri.auth.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MemberAuthReissueRequest(
    Long id,
    String refreshToken
) {
  public static MemberAuthReissueRequest of(Long id, String refreshToken){
    return MemberAuthReissueRequest.builder()
        .id(id)
        .refreshToken(refreshToken)
        .build();
  }
}
