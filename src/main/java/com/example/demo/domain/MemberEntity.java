package com.example.demo.domain;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@SequenceGenerator(
    name = "MEMBER_SEQ_GENERATOR", 
    sequenceName = "member_seq", // 사용할 시퀀스 이름
    initialValue = 1, // 시퀀스 시작 값
    allocationSize = 1 // 증가 값
)
@AllArgsConstructor // 필드의 모든 값을 매개변수로 받는 생성자 생성
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberEntity implements UserDetails {

   @Id // 기본키 할당
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
   private int memberNo;

   @Column(length = 50, nullable = false) // NOT NULL 설정
   private String memberId;

   @Column(length = 100, nullable = false) // NOT NULL 설정
   private String memberPasswd;

   @Column(length = 50, nullable = false) // NOT NULL 설정
   private String memberName;

   @Column(length = 11, nullable = false) // NOT NULL 설정
   private String memberTel;

   @Column(length = 50, nullable = false) // NOT NULL 설정
   private String memberEmail;

   @Builder(builderClassName = "MemberEntityBuilder") // 빌더 클래스 이름 지정
   public MemberEntity(String memberId, String memberPasswd, String memberName, String memberTel, String memberEmail) {
       this.memberId = memberId;
       this.memberPasswd = memberPasswd;
       this.memberName = memberName;
       this.memberTel = memberTel;
       this.memberEmail = memberEmail;
   }

   @Override // 권한 반환
   public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of(new SimpleGrantedAuthority("user"));
   }

   @Override
   public String getUsername() {
       return memberId;
   }

   @Override
   public String getPassword() {
       return memberPasswd;
   }

   // 계정 만료 여부 반환
   @Override
   public boolean isAccountNonExpired() {
       return true; // true -> 만료되지 않음
   }

   // 계정 잠금 여부 반환
   @Override
   public boolean isAccountNonLocked() {
       return true; // true -> 잠금되지 않음
   }

   // 패스워드 만료 여부 반환
   @Override
   public boolean isCredentialsNonExpired() {
       return true; // true -> 만료되지 않음
   }

   // 계정 사용 가능 여부 반환
   @Override
   public boolean isEnabled() {
       return true; // true -> 사용 가능
   }
}
