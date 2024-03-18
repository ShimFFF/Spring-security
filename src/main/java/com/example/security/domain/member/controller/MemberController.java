package com.example.security.domain.member.controller;

import com.example.security.domain.member.entity.Member;
import com.example.security.domain.member.entity.Role;
import com.example.security.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

@Controller // 컨트롤러 빈으로 등록 (뷰를 반환하기 위해 사용한다. 데이터를 바인딩하거나 가공하는 작업을 하지 않는다. @Controller는 주로 View를 반환하기 위해 사용한다.)
@RequiredArgsConstructor
public class MemberController {

    public final MemberRepository memberRepository;
    public final BCryptPasswordEncoder bCryptPasswordEncoder;

    /*@GetMapping("/members")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {
        System.out.println("Principal : " + principal);
        // iterator 순차 출력 해보기
        Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            System.out.println(auth.getAuthority());
        }

        return "유저 페이지입니다.";
    }*/

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "어드민 페이지입니다.";
    }

    @GetMapping("/login/form")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/join/form")
    public String join() {
        return "joinForm";
    }

    @PostMapping("/join/proc")
    public String joinProc(Member user) {
        System.out.println("회원가입 진행 : " + user);

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword); // 패스워드 암호화

        memberRepository.save(
                Member.builder()
                        .username(user.getUsername())
                        .password(encPassword)
                        .email(user.getEmail())
                        .role(Role.ROLE_USER)
                        .build()
        );

        return "redirect:/login/form";  // 리다이렉트 주소 (리다이렉트란? : 클라이언트가 다른 페이지로 이동하라는 명령을 받았을 때, 서버가 클라이언트에게 새로운 주소로 이동하라는 명령을 내리는 것)
    }
}
