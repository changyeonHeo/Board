package com.example.demo.controller;

import com.example.demo.domain.CommentEntity;
import com.example.demo.dto.CommentRequest;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public CommentEntity createComment(@RequestBody CommentRequest request) {
        return commentService.createComment(request.getBnum(), request.getContent());
    }

    // 대댓글 작성
    @PostMapping("/{commentId}/reply")
    public CommentEntity createReply(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        return commentService.createReply(commentId, request.getContent());
    }

    // 댓글 및 대댓글 조회
    @GetMapping
    public List<CommentEntity> getComments(@RequestParam Long bnum) {
        return commentService.getCommentsByBnum(bnum);
    }
}
