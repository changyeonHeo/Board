package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long bnum;
    private String content;
    private String writer;
    private LocalDateTime createdDate;

    // ✅ 부모 댓글 (ID만 보이고, 나머지 필드는 JSON에서 제외)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"parent", "replies"}) // 🔥 parent의 내부 필드 제외 (무한 루프 방지)
    private CommentEntity parent;

    // ✅ 대댓글 리스트
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CommentEntity> replies = new ArrayList<>();
}
