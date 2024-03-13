package com.example.security.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 컨트롤러 빈으로 등록 (뷰를 반환하기 위해 사용한다. 데이터를 바인딩하거나 가공하는 작업을 하지 않는다. @Controller는 주로 View를 반환하기 위해 사용한다.)
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

}
