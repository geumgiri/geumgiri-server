package com.tta.geumgiri.member.domain;

import com.tta.geumgiri.common.entity.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.tta.geumgiri.common.entity.BaseEntity;


@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String userId;

  private String password;

  private String name;

  @Enumerated(EnumType.STRING)
  private Role role;


  @Builder
  public Member(String name, String userId, String password, Role role) {
    this.name = name;
    this.userId = userId;
    this.password = password;
    this.role = role;
  }

}
