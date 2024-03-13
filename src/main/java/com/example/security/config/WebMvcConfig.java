package com.example.security.config;

import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 스프링부트에게 이 클래스를 설정 파일임을 알려준다. (Ioc 컨테이너 및 빈 설정)
public class WebMvcConfig implements WebMvcConfigurer {

    @Override // MustacheViewResolver를 설정한다. (머스테치 뷰 리졸버 설정, 리졸버란? 뷰의 이름에 해당하는 템플릿 파일을 찾아내는 역할을 하는 것)
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver = new MustacheViewResolver(); // 머스테치 뷰 리졸버 객체 생성

        resolver.setCharset("UTF-8");   // 뷰의 인코딩 설정 (UTF-8) (한국 언어를 표현하기 위해 UTF-8로 설정한다. 한국어는 3바이트로 표현되기 때문에 UTF-8로 설정해야 한글이 깨지지 않는다.)
        resolver.setContentType("text/html;charset=UTF-8");  // 뷰의 콘텐츠 타입 설정 (text/html;charset=UTF-8)
        resolver.setPrefix("classpath:/templates/");  // 뷰의 경로 설정 (classpath:/templates/)
        resolver.setSuffix(".html"); // 뷰의 확장자 설정 (.html)

        registry.viewResolver(resolver);  // 뷰 리졸버 등록
    }
}
