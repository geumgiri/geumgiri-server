package com.tta.geumgiri.auth.security.bridge;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtHandlerAdapterImpl implements JwtHandlerAdapter {

  private static final String USER_ID = "userId";
  private final RedisTemplate<String, String> redisTemplate;

  // 각 토큰의 만료 시간 설정
  private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 1000L * 60; // 1시간
  private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L * 14; // 14일

  @Value("${jwt.secret}")
  private String JWT_SECRET;

  // 토큰 발급 메서드, AccessToken과 RefreshToken 생성
  @Override
  public Token issueToken(Long userId) {
    return Token.of(
            generateAccessToken(userId),
            generateRefreshToken(userId)
    );
  }

  // AccessToken 생성 메서드
  private String generateAccessToken(Long userId) {
    return generateToken(userId, ACCESS_TOKEN_EXPIRATION_TIME);
  }

  // RefreshToken 생성 메서드
  private String generateRefreshToken(Long userId) {
    return generateToken(userId, REFRESH_TOKEN_EXPIRATION_TIME);
  }

  // 공통 토큰 생성 로직
  private String generateToken(Long userId, long expirationTime) {
    final Date now = new Date();
    final Date expiration = new Date(now.getTime() + expirationTime);

    return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setSubject(String.valueOf(userId))
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  // AccessToken 검증 메서드
  @Override
  public void validateAccessToken(String accessToken) {
    try {
      parseToken(accessToken);
    } catch (ExpiredJwtException e) {
      throw new UnauthorizedException(ErrorStatus.EXPIRED_ACCESS_TOKEN); // ACCESS_TOKEN의 만료 처리
    } catch (Exception e) {
      throw new UnauthorizedException(ErrorStatus.INVALID_ACCESS_TOKEN); // ACCESS_TOKEN의 유효성 검사 실패
    }
  }

  // RefreshToken 검증 메서드
  @Override
  public void validateRefreshToken(String refreshToken) {
    try {
      parseToken(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new UnauthorizedException(ErrorStatus.EXPIRED_REFRESH_TOKEN); // REFRESH_TOKEN의 만료 처리
    } catch (Exception e) {
      throw new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN); // REFRESH_TOKEN의 유효성 검사 실패
    }
  }

  // RefreshToken 비교 로직
  @Override
  public void equalsRefreshToken(String refreshToken, String storedRefreshToken) {
    if (!refreshToken.equals(storedRefreshToken)) {
      throw new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN);
    }
  }

  // 공통 파싱 메서드
  private void parseToken(String token) {
    JwtParser jwtParser = getJwtParser();
    jwtParser.parseClaimsJws(token);
  }

  // JWT 파싱을 위한 키 생성
  private JwtParser getJwtParser() {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build();
  }

  // 서명 키 생성 메서드
  private SecretKey getSigningKey() {
    String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
    return Keys.hmacShaKeyFor(encodedKey.getBytes());
  }

  // 토큰에서 사용자 정보 추출 (userId 반환)
  public Long getSubject(String token) {
    JwtParser jwtParser = getJwtParser();
    return Long.valueOf(jwtParser.parseClaimsJws(token)
            .getBody()
            .getSubject());
  }

  // 토큰에서 Claims 추출
  private Claims getBody(final String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  // 토큰으로부터 userId 추출
  public Long getUserFromJwt(String token) {
    Claims claims = getBody(token);
    return Long.valueOf(claims.get(USER_ID).toString());
  }

  public Token storeAccessToken(Long userId, String accessToken) {
    String key = "accessToken:" + userId;
    redisTemplate.opsForValue().set(key, accessToken, ACCESS_TOKEN_EXPIRATION_TIME / 1000, TimeUnit.SECONDS);
    return new Token(accessToken, null); // 예시로 accessToken만 포함된 Token 객체를 반환합니다.
  }
}
