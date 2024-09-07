package com.tta.geumgiri.auth.application;

import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.auth.domain.RefreshToken;
import com.tta.geumgiri.auth.persistence.RefreshTokenRepository;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthSignInRequest;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthSignUpRequest;
import com.tta.geumgiri.auth.presentation.dto.request.MemberAuthReissueRequest;
import com.tta.geumgiri.auth.presentation.dto.response.MemberAuthServiceResponse;
import com.tta.geumgiri.auth.security.bridge.JwtHandlerAdapter;
import com.tta.geumgiri.auth.security.bridge.Token;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import com.tta.geumgiri.common.entity.Role;
import com.tta.geumgiri.member.domain.Member;
import com.tta.geumgiri.member.persistence.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtHandlerAdapter jwtHandlerAdapter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 test")
    void testSignUp_Success() {
        MemberAuthSignUpRequest signUpRequest = new MemberAuthSignUpRequest("juel", "user1", "password", Role.PARENT);
        when(memberRepository.existsByUserId(signUpRequest.userId())).thenReturn(false); // false = 중복 x
        when(passwordEncoder.encode(signUpRequest.password())).thenReturn("encodedPassword");

        ResponseEntity<Object> response = authService.signUp(signUpRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입(아이디 중복) test")
    void testSignUp_DuplicateUserId() {
        MemberAuthSignUpRequest signUpRequest = new MemberAuthSignUpRequest("user1", "password", "USER", Role.CHILD);
        // true일 경우 = 해당 userId가 이미 존재함을 의미
        when(memberRepository.existsByUserId(signUpRequest.userId())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.signUp(signUpRequest));
        assertEquals(String.valueOf(ErrorStatus.DUPLICATE_USER_ID), exception.getMessage());
    }


 /*   @Test
    @DisplayName("로그인 test")
    void testSignIn_Success() {

        MemberAuthSignInRequest signInRequest = new MemberAuthSignInRequest("user1", "password");
        Member member = mock(Member.class);
        when(memberRepository.findByUserId(signInRequest.userId())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(signInRequest.password(), member.getPassword())).thenReturn(true);

        // 객체 생성
        Token token = mock(Token.class);

        when(jwtHandlerAdapter.issueToken(member.getId())).thenReturn(token);
        when(jwtHandlerAdapter.issueToken(member.getId())).thenReturn(token);

        MemberAuthServiceResponse response = authService.signIn(signInRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
    }


  */
    @Test
    @DisplayName("로그인(없는 계정 정보) test")
    void testSignIn_InvalidCredentials() {

        MemberAuthSignInRequest signInRequest = new MemberAuthSignInRequest("user1", "password");
        Member member = mock(Member.class);
        when(memberRepository.findByUserId(signInRequest.userId())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(signInRequest.password(), member.getPassword())).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.signIn(signInRequest));
        assertEquals(ErrorStatus.INVALID_CREDENTIALS, exception.getErrorStatus());
    }

    @Test
    @DisplayName("accessToken 재발급 받기 test")
    void testReissue_Success() {

        MemberAuthReissueRequest reissueRequest = new MemberAuthReissueRequest(1L, "reissueToken");

        // jwtHandlerAdapter의 메서드가 void 반환일 경우 doNothing() 사용
        doNothing().when(jwtHandlerAdapter).validateRefreshToken(reissueRequest.refreshToken());
        when(jwtHandlerAdapter.getSubject(reissueRequest.refreshToken())).thenReturn(1L);

        // refreshToken 저장소에서 찾기
        RefreshToken refreshToken = new RefreshToken(1L, "refreshToken");
        when(refreshTokenRepository.findById(1L)).thenReturn(Optional.of(refreshToken));

        // equalsRefreshToken도 void 반환일 경우 doNothing() 사용
        doNothing().when(jwtHandlerAdapter).equalsRefreshToken(reissueRequest.refreshToken(), refreshToken.getRefreshToken());

        // 새로운 토큰 발급
        Token token = new Token("newAccessToken", "newRefreshToken");
        when(jwtHandlerAdapter.issueToken(1L)).thenReturn(token);

        MemberAuthServiceResponse response = authService.reissue(reissueRequest);

        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
        assertEquals("newRefreshToken", response.refreshToken());
    }


    @Test
    @DisplayName("accessToken 재발급 받기(잘못된 refreshToken)일 때 test")
    void testReissue_InvalidRefreshToken() {

        MemberAuthReissueRequest reissueRequest = new MemberAuthReissueRequest(1L, "invalidRefreshToken");

        doThrow(new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN))
                .when(jwtHandlerAdapter).validateRefreshToken(reissueRequest.refreshToken());

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.reissue(reissueRequest));
        assertEquals(ErrorStatus.INVALID_REFRESH_TOKEN, exception.getErrorStatus());
    }

}

