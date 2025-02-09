package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private Long bnum;
    private String content;
    private String writer;
    private Long parentId = 0L; // ✅ 기본값 0L로 설정

    public boolean isReply() {
        return parentId != null && parentId > 0;
    }
}
