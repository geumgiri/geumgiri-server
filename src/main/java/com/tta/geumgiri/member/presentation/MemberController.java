package com.tta.geumgiri.member.presentation;


import com.tta.geumgiri.auth.application.AuthService;
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
  private final AuthService authService;


  @GetMapping("member/{memberId}")
  public ResponseEntity<MemberResponse> getMember(
          @PathVariable Long memberId,
          @RequestHeader("Authorization") String authHeader // Authorization 헤더에서 토큰 추출
  ) {
    String accessToken = authHeader.replace("Bearer ", ""); // Bearer 부분 제거

    // 사용자 접근 권한 검증
    authService.validateUserAccess(memberId, accessToken);

    return ResponseEntity.status(HttpStatus.OK)
            .body(memberService.getMember(memberId));
  }

  // 모든 멤버 조회 기능은 추후 구현
  /*
  @GetMapping("member/all")
  public ResponseEntity<List<MemberResponse>> getAllMember(
          @RequestHeader("Authorization") String authHeader
  ) {
    String accessToken = authHeader.replace("Bearer ", "");
    return ResponseEntity.status(HttpStatus.OK)
            .body(memberService.getAllMember());
  }
}

   */
}

