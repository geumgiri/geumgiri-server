package com.tta.geumgiri.auth.presentation.dto.request;


import com.tta.geumgiri.common.entity.Role;
import jakarta.validation.constraints.NotNull;

public record MemberAuthSignUpRequest(

    @NotNull
    String name,
    String userId,
    String password,
    Role role

) {

  public MemberAuthSignUpRequest(String name, String userId, String password, Role role) {
    this.name = name;
    this.userId = userId;
    this.password = password;
    this.role = role;
  }
}
