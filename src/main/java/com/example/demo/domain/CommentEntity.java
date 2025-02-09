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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties({"parent", "replies"})
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CommentEntity> replies = new ArrayList<>();

    // ✅ `Boolean`으로 변경 (null 허용)
    @Column(nullable = false)
    private Boolean isDeleted = false;  

    public boolean getIsDeleted() {
        return Boolean.TRUE.equals(isDeleted);  // ✅ null 방지
    }

    public void setIsDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }
}
