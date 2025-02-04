package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private Long parentId; // 대댓글인 경우 부모 댓글 ID
    private Long bnum; // 게시글 번호
    private String content; // 댓글 내용
}
