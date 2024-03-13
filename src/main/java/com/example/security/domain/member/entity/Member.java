package com.example.security.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;

import java.security.Timestamp;

/*
* 엔티티는 생성자가 필요하다.
* 롬복의 @NoArgsConstructor는 파라미터가 없는 기본 생성자를 만들어준다.
* 롬복의 @AllArgsConstructor는 모든 필드 값을 파라미터로 받는 생성자를 만들어준다.
* 롬복의 @Builder는 빌더 패턴을 사용할 수 있게 해준다.
*
* @AllArgsConstructor만 못쓰는 이유는 JPA 스펙상 엔티티는 기본 생성자가 필수이기 때문이다.
* 떄문에 @NoArgsConstructor를 사용하거나, @AllArgsConstructor와 @RequiredArgsConstructor를 같이 사용해야 한다.
* */

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member {
    @jakarta.persistence.Id
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role; //ROLE_USER, ROLE_ADMIN
    @CreationTimestamp
    private Timestamp createDate;

}