package com.tta.geumgiri.member.domain.util;

import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;

public class MemberUtil {


  public static MemberResponse fromEntity(Member member){
    return MemberResponse.builder()
        .id(member.getId())
        .name(member.getName())
        .role(member.getRole())
        .creditRatio(member.getCreditRatio())
        .account(member.getAccounts())
        .build();
  }

}
