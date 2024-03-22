package com.example.security.config.jwt;

import com.example.security.config.auth.PrincipalDetailsService;

public interface TokenPropertise {
    String jwtSecretKey="secret"; // JWT 비밀키 (원래는 숨겨야함)

    String refreshSecretKey="refresh"; // refresh 토큰 비밀키
    String AUTHORIZATION_HEADER = "Authorization"; // 헤더에 담길 토큰
    String REFRESH_HEADER = "refreshToken"; // 헤더에 담길 refresh 토큰
    long TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L;  // 유효기간 1일
    long REF_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L * 14L;  // 유효기간 14일

}
