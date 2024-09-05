package com.tta.geumgiri.auth.presentation.dto.request;


import jakarta.validation.constraints.NotNull;

public record MemberAuthSignUpRequest(

    @NotNull
    String name,
    String userId,
    String password

) {

  public MemberAuthSignUpRequest(String name, String userId, String password) {
    this.name = name;
    this.userId = userId;
    this.password = password;
  }
}
