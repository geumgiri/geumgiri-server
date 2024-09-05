package com.tta.geumgiri.member.presentation.dto.response;

import com.tta.geumgiri.common.entity.Role;
import lombok.Builder;


public record MemberResponse(

    Long id,
    String name,
    Role role
) {

  @Builder
  public MemberResponse(Long id, String name, Role role) {
    this.id = id;
    this.name = name;
    this.role = role;
  }

}
