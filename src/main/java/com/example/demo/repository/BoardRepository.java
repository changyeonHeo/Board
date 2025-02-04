package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	List<BoardEntity> findAllByOrderByBnumDesc(); // 최신순 목록 조회
    Page<BoardEntity> findAllByOrderByBnumDesc(Pageable pageable);
    Optional<BoardEntity> findTopByBnumLessThanOrderByBnumDesc(Long bnum); // 이전글
    Optional<BoardEntity> findTopByBnumGreaterThanOrderByBnumAsc(Long bnum); // 다음글



}
