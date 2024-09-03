package com.tta.geumgiri.member.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private String email;
    private String password;
}
