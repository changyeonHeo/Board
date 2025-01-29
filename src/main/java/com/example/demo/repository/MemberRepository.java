package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer>{
	Optional<MemberEntity> findByMemberId(String memberId);
	Optional<MemberEntity> findByMemberEmail(String email);
}
