package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.domain.MemberEntity;
import com.example.demo.dto.AddUserRequest;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // ✅ 회원가입 (중복 검사 포함)
    public boolean save(AddUserRequest dto) {
        if (isIdExist(dto.getMemberId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (isEmailExist(dto.getMemberEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (isPhoneExist(dto.getMemberTel())) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }

        memberRepository.save(MemberEntity.builder()
                .memberId(dto.getMemberId())
                .memberPasswd(bCryptPasswordEncoder.encode(dto.getMemberPasswd()))
                .memberName(dto.getMemberName())
                .memberTel(dto.getMemberTel())
                .memberEmail(dto.getMemberEmail())
                .build());
        
        return true;
    }

    // ✅ 아이디 중복 확인
    public boolean isIdExist(String memberId) {
        return memberRepository.findByMemberId(memberId).isPresent();
    }

    // ✅ 이메일 중복 확인
    public boolean isEmailExist(String email) {
        return memberRepository.findByMemberEmail(email).isPresent();
    }

    // ✅ 전화번호 중복 확인
    public boolean isPhoneExist(String phone) {
        return memberRepository.findByMemberTel(phone).isPresent();
    }
}
