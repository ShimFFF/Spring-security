package com.example.security.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean { // OncePerRequestFilter -> GenericFilterBean

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // refresh Token이 있다면 refresh Token 사용
        String token = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);

        if(token != null) {
            if (token.startsWith("Bearer ")) token = token.substring(7); // "Bearer " 제거

            if (((HttpServletRequest) request).getRequestURI() // 요청 URI가 "/members/token/refresh"이고 refresh token이 유효하다면
                    .equals("/members/refresh") && jwtTokenProvider.validateRefreshToken(token)) {
                // refresh token을 이용한 인증 정보를 SecurityContextHolder에 저장
                Authentication authentication = jwtTokenProvider.getRefreshAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            token =  jwtTokenProvider.resolveToken((HttpServletRequest) request); // 토큰이 있다면
            if (token != null) { // 토큰이 유효하다면
                if (token.startsWith("Bearer ")) token = token.substring(7); // "Bearer " 제거

                if (jwtTokenProvider.validateToken(token)) { // 토큰을 이용한 인증 정보를 SecurityContextHolder에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token); // 토큰을 이용한 인증 정보
                    SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 저장
                }
            }
        }

        chain.doFilter(request, response);
    }
}
