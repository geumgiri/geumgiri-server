package com.tta.geumgiri.member.presentation.dto.response;

import com.tta.geumgiri.common.entity.Role;
import lombok.Builder;

import java.util.List;


public record MemberResponse(

    Long id,
    String name,
    Role role,
    List account

) {

  @Builder
  public MemberResponse(Long id, String name, Role role, List account ) {
    this.id = id;
    this.name = name;
    this.role = role;
    this.account = account;
  }

}
