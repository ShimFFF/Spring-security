package com.example.security.domain.member.repository;

import com.example.security.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository를 상속받아 Member 엔티티를 관리하는 레포지토리를 생성한다.
// JpaRepository를 상속받으면 기본적인 CRUD 메소드를 제공받을 수 있다.
// JpaRepository의 제네릭 타입에는 엔티티와 id의 타입을 명시해주면 된다.
public interface MemberRepository extends JpaRepository<Member, Long> {
}
