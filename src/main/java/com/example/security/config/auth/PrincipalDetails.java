package com.example.security.config.auth;

import com.example.security.domain.member.entity.Member;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 로그인 요청("/login")이 오면 낚아 채서 로그인을 진행하고 완료가 되면
// 오브젝트를 스프링 시큐리티의 고유한 세션 저장소에 저장을 해준다.
// Security Session(내부 Authentication(내부 UserDetails))
// Authentication 객체에 저장할 수 있는 유일한 타입
@Data
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final Member user;

    // 생성자
    // UserDetails를 상속받아 PrincipalDetails를 생성한다.


    // 권한을 리턴
    // 권한 형식 : ROLE_USER, ROLE_ADMIN
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return authorities;
    }

    // 유저 정보 리턴
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 비밀번호가 만료되지 않았는지 리턴한다.
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화 되어있는지 리턴한다.
        // 휴먼 계정 등 비활성 계정일 때 false로 리턴하면 됨
    }
}