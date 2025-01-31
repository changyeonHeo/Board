package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
    @SequenceGenerator(name = "board_seq", sequenceName = "board_seq", allocationSize = 1)
    private Long bnum;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column
    private String bimage; // 이미지 경로

    @Transient
    private Date formattedDate; // JSP에서 사용하기 위한 변환된 날짜

    // ✅ @Builder 사용 시 모든 필드에 대해 초기화 생성자 필요
    @Builder
    public BoardEntity(Long bnum, String title, String content, String writer, LocalDateTime createdDate, String bimage) {
        this.bnum = bnum;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdDate = (createdDate != null) ? createdDate : LocalDateTime.now();
        this.bimage = bimage;
    }
}
