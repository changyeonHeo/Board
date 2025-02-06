package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "comments")
@NoArgsConstructor // ✅ JPA를 위한 기본 생성자 추가
@AllArgsConstructor
@Builder // ✅ @Builder 사용 시 @AllArgsConstructor 필요
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bnum; // 게시글 번호

    @Column(nullable = false, length = 500)
    private String content; // 댓글 내용

    @Column(nullable = false)
    private String writer; // 작성자

    @Column(nullable = false)
    private LocalDateTime createdDate; // 작성일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parent; // 부모 댓글 (대댓글을 위해)

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // ✅ 초기화 필요 (NPE 방지)
    private List<CommentEntity> replies = new ArrayList<>();

    // ✅ 생성자 추가
    public CommentEntity(Long bnum, String content, String writer, LocalDateTime createdDate, CommentEntity parent) {
        this.bnum = bnum;
        this.content = content;
        this.writer = writer;
        this.createdDate = createdDate;
        this.parent = parent;
    }
}
