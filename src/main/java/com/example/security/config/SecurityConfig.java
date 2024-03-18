package com.example.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
public class SecurityConfig{

    // IoC 빈(bean)을 등록 -> 스프링 컨테이너에서 관리 -> 필요할 때 주입 -> 싱글톤으로 관리 -> 메모리에 한 번만 올라감
    public static BCryptPasswordEncoder encodePwd;
    /*
    * 이전 방식  : WebSecurityConfigurerAdapter를 상속하고 configure매소드를 오버라이딩하여 설정
    * 지금 방식 : SecurityFilterChain을 리턴하는 메소드를 빈에 등록하는 방식(컴포넌트 방식으로 컨테이너가 관리)
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                authorize//.requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
                        //.requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
                        .requestMatchers("/admin/**").hasRole("ADMIN")// ROLE_ADMIN만 들어갈 수 있는 주소
                        .requestMatchers("/login/**", "/join/**").permitAll() // 로그인, 회원 가입은 누구나 들어갈 수 있는 주소
                        .anyRequest().authenticated() // 인증만 되면 들어갈 수 있는 주소
                        // .anyRequest().authenticated() // 그 외의 주소는 모두 인증을 받아야 들어갈 수 있음
                );
        return http.build();
    }
}
