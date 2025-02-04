package com.example.demo.repository;

import com.example.demo.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBnumAndParentIsNullOrderByCreatedDateDesc(Long bnum);
}
