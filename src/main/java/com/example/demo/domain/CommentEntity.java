package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private Boolean isDeleted = false; // ✅ 기본값 false 설정

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CommentEntity> replies;

    // ✅ 🔹 LocalDateTime → Date 변환용 메서드 추가
    public Date getFormattedDate() {
        return Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
