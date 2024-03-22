package com.example.security.config.jwt;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.auth.PrincipalDetailsService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private String jwtSecretKey; // JWT 비밀키

    private String refreshSecretKey; // refresh 토큰 비밀키

    private final PrincipalDetailsService principalDetailsService;

    @PostConstruct
    protected void init() { // Base64로 인코딩
        jwtSecretKey = Base64.getEncoder().encodeToString(TokenPropertise.jwtSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(TokenPropertise.refreshSecretKey.getBytes());
    }

    // 토큰 생성
    public String generateAccessToken(Long memberId) {
        Date now = new Date(); // 현재 시간
        Date accessTokenExpirationTime = new Date(now.getTime() + TokenPropertise.TOKEN_VALID_TIME); // 토큰 만료 시간

        Claims claims = Jwts.claims(); // claim 설정 (클레임은 JWT에 담고 싶은 정보)
        claims.put("memberId", memberId); // memberId를 담음

        return Jwts.builder() // JWT 토큰 생성
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(accessTokenExpirationTime) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey) // 토큰 암호화 알고리즘, secret값 세팅
                .compact();
    }

    public String generateRefreshToken(Long memberId) {
        Date now = new Date();
        Date refreshTokenExpirationTime = new Date(now.getTime() + TokenPropertise.REF_TOKEN_VALID_TIME);

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(refreshTokenExpirationTime)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    public TokenInfo generateToken(Long memberId) {

        String accessToken = generateAccessToken(memberId); // accessToken 생성
        String refreshToken = generateRefreshToken(memberId); // refreshToken 생성

        return new TokenInfo(accessToken, refreshToken); // TokenInfo 객체에 담아서 반환
    }

    // 토큰에서 회원 정보 추출
    public Authentication getAuthentication(String token) {
        try {
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername( // 회원 정보 조회
                    getMemberIdByToken(token)); // 토큰에서 회원 정보 추출
            return new UsernamePasswordAuthenticationToken(principalDetails,
                    "", principalDetails.getAuthorities()); // 인증 객체 생성
        } catch (UsernameNotFoundException exception) {
            throw new UsernameNotFoundException("UNSUPPORTED_JWT");
        }
    }

    // refresh 토큰에서 회원 정보 추출
    public Authentication getRefreshAuthentication(String token) {
        try {
            PrincipalDetails principalDetails = principalDetailsService.loadUserByUsername(
                    getMemberIdByRefreshToken(token)); // refresh 토큰에서 회원 정보 추출
            return new UsernamePasswordAuthenticationToken(principalDetails,
                    "", principalDetails.getAuthorities()); // 인증 객체 생성
        } catch (UsernameNotFoundException exception) {
            throw new UsernameNotFoundException("UNSUPPORTED_JWT");
        }
    }

    // 토큰에서 회원 정보 추출
    public String getMemberIdByToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token). // 토큰 파싱
                getBody().get("memberId").toString(); // memberId 추출
    }
    public String getMemberIdByRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token).
                getBody().get("memberId").toString();
    }

    // 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(TokenPropertise.AUTHORIZATION_HEADER);
    }

    // refresh 토큰 추출
    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(TokenPropertise.REFRESH_HEADER);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new SecurityException("INVALID_ACCESS_TOKEN");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException("EXPIRED_MEMBER_JWT");
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new UnsupportedJwtException("UNSUPPORTED_JWT");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("EMPTY_JWT");
        }
    }

    // refresh 토큰 유효성 검사
    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new SecurityException("INVALID_REFRESH_TOKEN");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException("EXPIRED_MEMBER_JWT");
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new UnsupportedJwtException("UNSUPPORTED_JWT");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("EMPTY_JWT");
        }
    }
}