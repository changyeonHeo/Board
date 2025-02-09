package com.example.demo.repository;

import com.example.demo.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // ✅ createdDate 기준 내림차순 정렬 (최신 댓글이 위로)
    List<CommentEntity> findByBnumAndParentIsNullOrderByCreatedDateDesc(Long bnum);
}