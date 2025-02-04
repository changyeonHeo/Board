package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bnum; // 게시글 번호
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentEntity parent; // 부모 댓글

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> replies = new ArrayList<>(); // 대댓글 목록

    private LocalDateTime createdDate = LocalDateTime.now(); // 생성 날짜
}
