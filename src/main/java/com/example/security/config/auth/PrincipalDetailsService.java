package com.example.security.config.auth;

import com.example.security.domain.member.entity.Member;
import com.example.security.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberEntity = memberRepository.findById(Long.getLong(username)); //username은 아이디가 들어 옴
                //.orElseThrow(() -> new RestApiException(MemberErrorCode.EMPTY_MEMBER));
        return memberEntity.map(PrincipalDetails::new).orElse(null);

    }
}