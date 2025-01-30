package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
