package com.tta.geumgiri.auth.security.filter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.tta.geumgiri.auth.application.exception.UnauthorizedException;
import com.tta.geumgiri.auth.domain.UserAuthentication;
import com.tta.geumgiri.auth.security.bridge.JwtHandlerAdapter;
import com.tta.geumgiri.common.dto.response.responseEnum.ErrorStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.tta.geumgiri.auth.domain.UserAuthentication.createUserAuthentication;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtHandlerAdapter jwtHandlerAdapter;
  private static final List<String> EXCLUDE_URLS = Arrays.asList("/api/v1/auth", "/api/v1/auth/**");

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String requestURI = request.getRequestURI();
    if (EXCLUDE_URLS.stream().anyMatch(requestURI::startsWith)) {
      filterChain.doFilter(request, response); // 특정 URL에 대해서 필터 실행 X
      return;
    }

    final String accessToken = getAccessToken(request);
    jwtHandlerAdapter.validateAccessToken(accessToken);
    doAuthentication(request, jwtHandlerAdapter.getSubject(accessToken));
    filterChain.doFilter(request, response);
  }

  private String getAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring("Bearer ".length());
    }
    throw new UnauthorizedException(ErrorStatus.INVALID_ACCESS_TOKEN);
  }

  private void doAuthentication(HttpServletRequest request, Long userId) {
    UserAuthentication authentication = createUserAuthentication(userId);
    createAndSetWebAuthenticationDetails(request,authentication);
    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(authentication);
  }

  private void createAndSetWebAuthenticationDetails(HttpServletRequest request, UserAuthentication authentication) {
    //인증 세부 정보 설정
    WebAuthenticationDetailsSource webAuthenticationDetailsSource = new WebAuthenticationDetailsSource();
    WebAuthenticationDetails webAuthenticationDetails = webAuthenticationDetailsSource.buildDetails(request);
    authentication.setDetails(webAuthenticationDetails);
  }
}
