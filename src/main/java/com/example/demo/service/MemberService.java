package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.MemberEntity;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int save(AddUserRequest dto) {
        // MemberEntity 빌더를 사용하여 사용자 저장
        return memberRepository.save(MemberEntity.builder()
                .memberId(dto.getMemberId()) // 사용자 ID
                .memberPasswd(bCryptPasswordEncoder.encode(dto.getMemberPasswd())) // 암호화된 비밀번호
                .build()).getMemberNo(); // 저장된 사용자의 기본 키 반환
    }
    public boolean isEmailExist(String email) {
        return memberRepository.findByMemberEmail(email).isPresent();
    }

}

