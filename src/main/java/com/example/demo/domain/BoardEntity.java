package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함한 생성자 추가
@Builder
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
    @SequenceGenerator(name = "board_seq", sequenceName = "board_seq", allocationSize = 1)
    private Long bnum; // 게시글 번호 (Primary Key)

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false, length = 5000)
    private String content; // 내용

    @Column(nullable = false)
    private String writer; // 작성자

    @Column(nullable = false)
    private LocalDateTime date; // 작성 날짜

    @Column
    private String bimage; // 이미지 경로

    // @Builder.Default를 사용해 기본값 설정 (Builder 사용 시 자동 적용)
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
}
