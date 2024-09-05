package com.tta.geumgiri.auth.security.bridge;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtHandlerAdapterImpl implements JwtHandlerAdapter {

  private static final String USER_ID = "userId";
  private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L * 14;
  private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 24 * 60 * 60 * 1000L * 14;

  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Override
  public Token issueToken(Long userId) {
    return Token.of(generateToken(userId, true),
        generateToken(userId, false));
  }

  @Override
  public Long getSubject(String token) {
    JwtParser jwtParser = getJwtParser();
    return Long.valueOf(jwtParser.parseClaimsJws(token)
        .getBody()
        .getSubject()
    );
  }

  @Override
  public void validateAccessToken(String accessToken) {
    try {
      parseToken(accessToken);
    } catch (ExpiredJwtException e) {
      throw new UnauthorizedException(ErrorStatus.EXPIRED_REFRESH_TOKEN);
    } catch (Exception e) {
      throw new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN);
    }
  }

  @Override
  public void validateRefreshToken(String refreshToken) {
    try {
      parseToken(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new UnauthorizedException(ErrorStatus.EXPIRED_REFRESH_TOKEN);
    } catch (Exception e) {
      throw new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN);
    }
  }

  @Override
  public void equalsRefreshToken(String refreshToken, String storedRefreshToken) {
    if (!refreshToken.equals(storedRefreshToken)) {
      throw new UnauthorizedException(ErrorStatus.INVALID_REFRESH_TOKEN);
    }
  }

  private String generateToken(Long userId, boolean isAccessToken) {
    final Date now = new Date();
    final Date expiration = generateExpiration(now, isAccessToken);
    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setSubject(String.valueOf(userId))
        .setIssuedAt(expiration)
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Date generateExpiration(Date now, boolean isAccessToken) {
    return new Date(now.getTime() + calculateExpirationDate(isAccessToken));
  }

  private long calculateExpirationDate(boolean isAccessToken) {
    if (isAccessToken) {
      return ACCESS_TOKEN_EXPIRATION_TIME;
    }
    return REFRESH_TOKEN_EXPIRATION_TIME;
  }

  private JwtParser getJwtParser() {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build();
  }

  private void parseToken(String token) {
    JwtParser jwtParser = getJwtParser();
    jwtParser.parseClaimsJws(token);
  }

  private SecretKey getSigningKey() {
    String encodedKey = Base64.getEncoder()
        .encodeToString(JWT_SECRET.getBytes()); //SecretKey 통해 서명 생성
    return Keys.hmacShaKeyFor(
        encodedKey.getBytes());   //일반적으로 HMAC (Hash-based Message Authentication Code) 알고리즘 사용
  }

  private String encodeSecretKey() {
    return Base64
        .getEncoder()
        .encodeToString(JWT_SECRET.getBytes());
  }

//  public JwtValidationType validateToken(String token) {
//    try {
//      final Claims claims = getBody(token);
//      return JwtValidationType.VALID_JWT;
//    } catch (MalformedJwtException ex) {
//      return JwtValidationType.INVALID_JWT_TOKEN;
//    } catch (ExpiredJwtException ex) {
//      return JwtValidationType.EXPIRED_JWT_TOKEN;
//    } catch (UnsupportedJwtException ex) {
//      return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
//    } catch (IllegalArgumentException ex) {
//      return JwtValidationType.EMPTY_JWT;
//    }
//  }

  private Claims getBody(final String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public Long getUserFromJwt(String token) {
    Claims claims = getBody(token);
    return Long.valueOf(claims.get(USER_ID).toString());
  }

//  public String refreshAccessToken(String refreshToken) {
//    Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
//    if (refreshTokenOpt.isPresent()) {
//      RefreshToken token = refreshTokenOpt.get();
//      UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUserName());
//      Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//      String newAccessToken = generateAccessToken(authentication, ACCESS_TOKEN_EXPIRATION_TIME);
//      token.setAccessToken(newAccessToken);
//      refreshTokenRepository.save(token);
//      return newAccessToken;
//    }
//    throw new IllegalArgumentException("Invalid refresh token");
//  }
}
