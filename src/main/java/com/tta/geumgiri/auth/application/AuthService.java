package com.tta.geumgiri.auth.application;

import lombok.RequiredArgsConstructor;
import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.auth.domain.RefreshToken;
import com.tta.geumgiri.auth.persistence.RefreshTokenRepository;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthReissueRequest;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthSignInRequest;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthSignUpRequest;
import com.tta.geumgiri.auth.presentation.dto.response.MemberAuthServiceResponse;
import com.tta.geumgiri.auth.security.bridge.JwtHandlerAdapter;
import com.tta.geumgiri.auth.security.bridge.Token;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.exception.NotFoundException;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.persistence.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final JwtHandlerAdapter jwtHandlerAdapter;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;

  public ResponseEntity<Object> signUp(MemberAuthSignUpRequest request){

    if (memberRepository.existsByUserId(request.userId())) {
      throw new IllegalArgumentException(String.valueOf(ErrorStatus.DUPLICATE_USER_ID));
    }

    String encodedPassword = passwordEncoder.encode(request.password());

    Member member = Member.builder()
        .name(request.name())
        .userId(request.userId())
        .password(encodedPassword)
        .role(request.role())
        .build();

    memberRepository.save(member);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  public MemberAuthServiceResponse signIn(MemberAuthSignInRequest request){
    Member findMember = findMemberBy(request.userId());

    if (!passwordEncoder.matches(request.password(), findMember.getPassword())) {
      throw new UnauthorizedException(ErrorStatus.INVALID_CREDENTIALS);
    }

    Token issuedToken = issueTokenAndStoreRefreshToken(findMember.getId());
    return MemberAuthServiceResponse.of(issuedToken.accessToken(), issuedToken.refreshToken());
  }

  public MemberAuthServiceResponse reissue(MemberAuthReissueRequest request){
    try{
      jwtHandlerAdapter.validateRefreshToken(request.refreshToken());
      Long memberId = jwtHandlerAdapter.getSubject(request.refreshToken());
      RefreshToken findRefreshToken = findRefreshTokenBy(memberId);
      jwtHandlerAdapter.equalsRefreshToken(request.refreshToken(), findRefreshToken.getRefreshToken());
      Token issuedToken = issueTokenAndStoreRefreshToken(memberId);
      return MemberAuthServiceResponse.of(issuedToken.accessToken(), issuedToken.refreshToken());
    }catch (UnauthorizedException e){
      //sign out 처리
      throw e;
    }
  }

  private RefreshToken findRefreshTokenBy(Long userId) {
    return refreshTokenRepository.findById(userId)
        .orElseThrow(() -> new UnauthorizedException(ErrorStatus.EXPIRED_REFRESH_TOKEN));
  }

  private Member findMemberBy(String userId) {
    return memberRepository.findByUserId(userId)
        .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND));
  }

  private Token issueTokenAndStoreRefreshToken(Long userId) {
    Token issuedToken = jwtHandlerAdapter.issueToken(userId);
    RefreshToken refreshToken = RefreshToken.builder()
        .id(userId)
        .refreshToken(issuedToken.refreshToken())
        .build();
    refreshTokenRepository.save(refreshToken);
    return issuedToken;
  }


}
