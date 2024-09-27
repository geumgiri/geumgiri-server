package com.tta.geumgiri.member.application;

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
    // 멤버를 찾지 못한 경우 NotFoundException 발생
    Member foundMember = memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));

    return MemberUtil.fromEntity(foundMember);
  }

  public List<MemberResponse> getAllMember() {
    List<Member> allMembers = memberRepository.findAll();

    // 멤버가 없을 경우 NotFoundException 발생
    if (allMembers.isEmpty()) {
      throw new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND);  // 적절한 에러 상태로 수정
    }

    return allMembers.stream()
            .map(MemberUtil::fromEntity)
            .toList();
  }
}
