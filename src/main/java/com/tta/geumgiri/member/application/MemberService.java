package com.tta.geumgiri.member.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.NotFoundException;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.domain.util.MemberUtil;
import com.tta.geumgiri.member.persistence.MemberRepository;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public MemberResponse getMember(Long id) {

    Member foundMember = memberRepository.findById(id)
        .orElseThrow(() ->
            new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));
    return MemberUtil.fromEntity(foundMember);
  }

  public List<MemberResponse> getAllMember() {

    List<Member> allMembers = memberRepository.findAll();
    if (allMembers.isEmpty()) {
      throw new EntityNotFoundException("존재하는 유저가 없습니다.");
    }

    return allMembers.stream()
        .map(MemberUtil::fromEntity)
        .toList();
  }


}
