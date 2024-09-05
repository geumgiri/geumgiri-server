package com.tta.geumgiri.member.presentation;


import lombok.RequiredArgsConstructor;
import com.tta.geumgiri.member.application.MemberService;
import com.tta.geumgiri.member.presentation.dto.response.MemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1")
public class MemberController {

  private final MemberService memberService;

  @GetMapping("member/{memberId}")
  public ResponseEntity<MemberResponse> getMember(
      @PathVariable Long memberId) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(memberService.getMember(memberId));
  }

  @GetMapping("member/all")
  public ResponseEntity<List<MemberResponse>> getAllMember() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(memberService.getAllMember());
  }

}

