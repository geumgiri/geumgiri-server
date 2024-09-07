package com.tta.geumgiri.auth.presentation;

import lombok.RequiredArgsConstructor;
import com.tta.geumgiri.auth.application.AuthService;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthReissueRequest;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthSignInRequest;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthSignUpRequest;
import com.tta.geumgiri.auth.presentation.dto.response.MemberAuthServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {


  private final AuthService authService;

  @PostMapping("/auth/signup")
  public ResponseEntity<?> signup(
      @RequestBody final MemberAuthSignUpRequest request
  ){
    final ResponseEntity<Object> response = authService.signUp(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  @PostMapping("/auth/signin")
  public ResponseEntity<?> login(@RequestBody MemberAuthSignInRequest request) {
    final MemberAuthServiceResponse response = authService.signIn(request);
    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }

  @PostMapping("/auth/reissue")
  public ResponseEntity<?> refresh(@RequestBody MemberAuthReissueRequest request) {
      final MemberAuthServiceResponse response = authService.reissue(request);
      return ResponseEntity.ok(response);
  }

}
